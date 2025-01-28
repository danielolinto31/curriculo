package br.com.curriculo.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.curriculo.arq.commons.utils.FacesUtil;
import br.com.curriculo.arq.controls.AbstractCrudBean;
import br.com.curriculo.arq.exception.ServiceBusinessException;
import br.com.curriculo.arq.persistence.PageData;
import br.com.curriculo.domain.Arquivo;
import br.com.curriculo.domain.Curriculo;
import br.com.curriculo.domain.Escolaridade;
import br.com.curriculo.service.ArquivoService;
import br.com.curriculo.service.CurriculoService;
import br.com.curriculo.util.Utils;
import lombok.Getter;
import lombok.Setter;

@Controller
@Getter
@Setter
@Scope("view")
public class FormularioBean extends AbstractCrudBean<Curriculo, CurriculoService> {

	private static final long serialVersionUID = 1L;

	private static final String ERRO = "Erro: ";
	private static final String MESSAGE_ERRO_ARQUIVO = "Não foi possível fazer o upload do arquivo";
	private static final String MESSAGE_ERRO_IP = "Erro ao capturar o IP do usuário";
	private static final String EMAIL_HOST = Utils.getConfig("mail.host");
	private static final String EMAIL_PORT = Utils.getConfig("mail.port");
	private static final String EMAIL_AUTH = Utils.getConfig("mail.auth");
	private static final String EMAIL_TLS = Utils.getConfig("mail.tls");
	private static final String EMAIL_REMETENTE = Utils.getConfig("mail.user");
	private static final String EMAIL_PASSWORD = Utils.getConfig("mail.password");

	private final ArquivoService arquivoService;

	private String ip;
	private Escolaridade escolaridade;
	private List<Curriculo> curriculoList;
	private List<Escolaridade> escolaridadeList;

	private transient HttpServletRequest request;

	@Inject
	public FormularioBean(CurriculoService service, ArquivoService arquivoService) {
		super(service);
		this.arquivoService = arquivoService;
	}

	@Override
	public void onInit() {
		this.curriculoList = new ArrayList<>();
		this.escolaridadeList = Arrays.asList(Escolaridade.values());
	}

	@Override
	protected PageData<Curriculo> loadSearchData(int first, int pageSize, String sortField, boolean sortOrder) {
		this.curriculoList.clear();
		this.curriculoList.addAll(getService().findByAttributes(getEntityToSearch()));
		return super.loadSearchData(first, pageSize, sortField, sortOrder);
	}

	public String getClientIp(HttpServletRequest request) {
		String ipAddress = "";

		try {
			InetAddress addr = InetAddress.getLocalHost();

			if (request == null) {
				ipAddress = addr.getHostAddress();
			} else {
				ipAddress = request.getHeader("X-FORWARDED-FOR");
				if (ipAddress == null) {
					ipAddress = request.getRemoteAddr();
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			addError(MESSAGE_ERRO_IP);
		}

		return ipAddress;
	}

	@Override
	public void loadInsertMode() {
		getEntity().setArquivo(new Arquivo());
		super.loadInsertMode();
	}

	@Override
	public void executeServiceInsert(Curriculo entityToInsert) throws ServiceBusinessException {
		if (null != entityToInsert.getId() && entityToInsert.getId() == 0) {
			entityToInsert.setId(null);
		}

		entityToInsert.setIp(getClientIp(request));
		entityToInsert.setDataEnvio(new Date());
		super.executeServiceInsert(entityToInsert);
	}

	@Override
	public void delete(Curriculo entityToDelete) {
		super.delete(entityToDelete);
		onInit();
		getEntity().setArquivo(null);
		entityNewInstance();
		entityToSearchNewInstance();
		setEntity(null);
	}

	@Override
	protected void onAfterSave(Curriculo entitySaved) throws ServiceBusinessException {
		enviarEmail();
		entityNewInstance();
		entityToSearchNewInstance();
		getEntity().setArquivo(null);
		setAction(Action.INSERT);
		loadInsertMode();
		super.onAfterSave(entitySaved);
	}

	public void limparCampos() {
		entityNewInstance();
		entityToSearchNewInstance();
		setAction(Action.INSERT);
		loadInsertMode();
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			this.log.info("Uploaded: {}", event.getFile().getFileName());
			UploadedFile uploadedFile = event.getFile();
			Arquivo arquivo = new Arquivo();
			arquivo.setContentType(".pdf");
			arquivo.setSize((int) uploadedFile.getSize());
			arquivo.setFile(uploadedFile.getContent());
			arquivo.setNome(uploadedFile.getFileName());
			arquivoService.insert(arquivo);
			getEntity().setArquivo(arquivo);
		} catch (Exception e) {
			e.printStackTrace();
			addError(MESSAGE_ERRO_ARQUIVO);
		}
	}

	public void download(Curriculo curriculo) {
		if (null != curriculo && null != curriculo.getArquivo()) {
			try {
				FacesUtil.provideFileView(curriculo.getArquivo().getNome(), curriculo.getArquivo().getFile());
			} catch (IOException e) {
				addError("Não foi possível disponibilizar o arquivo");
			}
		}
	}

	public void enviarEmail() {
		boolean isPdf = getEntity().getArquivo() != null && getEntity().getArquivo().getContentType() != null
				&& !getEntity().getArquivo().getContentType().isEmpty()
				&& ".pdf".equals(getEntity().getArquivo().getContentType());
		String contentType = isPdf ? "application/pdf" : "application/msword";

		StringBuilder conteudo = new StringBuilder();
		conteudo.append("<html><body>");
		conteudo.append("<p><strong>E-mail enviado automaticamente, não respondê-lo.</strong></p>");
		conteudo.append("<p>Seguem os dados informados:</p>");
		conteudo.append("<p><ul>");
		conteudo.append("<li><strong>Nome:</strong> " + getEntity().getNome() + "</li>");
		conteudo.append("<li><strong>E-mail:</strong> " + getEntity().getEmail() + "</li>");
		conteudo.append("<li><strong>Telefone:</strong> " + getEntity().getTelefone() + "</li>");
		conteudo.append("<li><strong>Escolaridade:</strong> " + getEntity().getEscolaridade().getDescricao() + "</li>");
		conteudo.append("<li><strong>Cargo Desejado:</strong> " + getEntity().getCargoDesejado() + "</li>");
		conteudo.append("<li><strong>Observações:</strong> " + getEntity().getObservacoes() + "</li>");
		conteudo.append("</ul></p>");
		conteudo.append("<p><i>E-mail enviado pelo sistema <strong>Currículo</strong><br />");
		conteudo.append("Desenvolvido por: Pedro Lucas Ferreira de Araújo</i></p>");
		conteudo.append("</body></html>");

		Properties props = new Properties();
		props.put("mail.smtp.host", EMAIL_HOST);
		props.put("mail.smtp.port", EMAIL_PORT);
		props.put("mail.smtp.auth", EMAIL_AUTH);
		props.put("mail.smtp.starttls.enable", EMAIL_TLS);

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_REMETENTE, EMAIL_PASSWORD);
			}
		});

		try {
			byte[] arquivo = getEntity().getArquivo().getFile();
			Multipart mp = new MimeMultipart();
			MimeBodyPart conteudoHtml = new MimeBodyPart();
			conteudoHtml.setContent(conteudo.toString(), "text/html; charset=UTF-8");
			mp.addBodyPart(conteudoHtml);

			MimeBodyPart anexo = new MimeBodyPart();
			ByteArrayDataSource source = new ByteArrayDataSource(arquivo, contentType);
			DataHandler handler = new DataHandler(source);
			anexo.setDataHandler(handler);
			anexo.setFileName(getEntity().getArquivo().getNome());
			mp.addBodyPart(anexo);

			InternetAddress[] destinariosCc = { new InternetAddress(getEntity().getEmail(), getEntity().getNome()) };
			InternetAddress destinatario = new InternetAddress(getEntity().getEmail(), getEntity().getNome());

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EMAIL_REMETENTE, "E-mail automático"));
			msg.addRecipient(Message.RecipientType.TO, destinatario);
			msg.addRecipients(Message.RecipientType.CC, destinariosCc);
			msg.setSubject("Currículo - " + getEntity().getNome());
			msg.setContent(mp);

			Transport.send(msg);
		} catch (MessagingException | UnsupportedEncodingException e) {
			addError("Erro ao enviar e-mail");
			e.printStackTrace();
			log.error(ERRO, e);
		}
	}

}
