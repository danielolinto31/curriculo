# Formulário-Java

## Sobre o Projeto

Este projeto foi desenvolvido como parte de um teste prático para a **UGTSIC**, com o objetivo de criar um formulário para envio de currículos. Durante o desenvolvimento, encarei desafios técnicos que me permitiram crescer profissionalmente, aprofundar meu conhecimento em **Java**, e testar minha capacidade de aprendizagem.

---

## Tecnologias, Frameworks e Ferramentas Utilizadas

- **IDE**: Eclipse  
- **Linguagem de Programação**: Java  
- **Ferramentas**:  
  - **Maven**: Para gestão de dependências e compilação.  
  - **Lombok**: Automatização de getters, setters e simplificação do código.  
  - **PrimeFaces**: Para criação de layouts e validação de campos obrigatórios.  
- **Servidor de Aplicação Local**: Tomcat 9  
- **Banco de Dados**: H2 (banco em memória, com suporte a SQL).  
- **Funcionalidades Adicionais**:  
  - **reCaptcha**: Para aumentar a segurança contra bots.  
  - **Limpar Formulário**: Opção para redefinir os campos.  
  - **Validação de Campos**: Verificação de caracteres e campos obrigatórios.  

---

## Objetivos do Projeto

1. Desenvolver uma aplicação funcional utilizando **Java**, mesmo com conhecimento limitado na linguagem.  
2. Implementar um formulário capaz de receber e armazenar currículos em **.PDF**.  
3. Proporcionar uma interface amigável com **PrimeFaces**.  
4. Utilizar um banco de dados leve e em memória para facilitar o desenvolvimento e testes.  

---

## Lições Aprendidas

1. **Superação de Desafios**: Trabalhar com uma linguagem na qual eu tinha pouca experiência foi um desafio significativo, mas recompensador.  
2. **Colaboração**: Contar com o apoio de professores e um familiar experiente foi essencial para resolver problemas e aprender boas práticas.  
3. **Resiliência**: Enfrentar erros e encontrar soluções me ajudou a aprimorar minha capacidade de resolver problemas de forma independente.  

---

## Como Configurar e Executar o Projeto

1. **Clone o repositório** na IDE Eclipse ou outra de sua preferência.  
2. Execute o comando `mvn clean install -U` para garantir que as dependências sejam resolvidas corretamente.  
3. Certifique-se de usar uma versão compatível do **Java**:  
   - O projeto foi desenvolvido com **JDK 8**, mas o **Tomcat 9**
4. Inicie o servidor **Tomcat 9** e acesse a aplicação no navegador.  

---

## Problemas Conhecidos

2. **Banco de Dados**: A aplicação utiliza o banco H2 em memória, o que limita a persistência dos dados após reiniciar o servidor.  
3. **Compatibilidade com JDK**: A aplicação apresenta problemas ao utilizar versões superiores ao **JDK 11**. Para corrigir, atualize o projeto clicando com o botão direito em:  
   - `Projeto -> Maven -> Update Project -> Force Update`.  
   Isso restaurará as configurações para **JDK 1.8**, permitindo que a aplicação funcione perfeitamente.  

---

## Conclusão

Este projeto foi uma experiência valiosa que demonstrou minha capacidade de adaptação e aprendizado sob pressão. Apesar das limitações técnicas enfrentadas, consegui entregar uma aplicação funcional e ampliar meu conhecimento em **Java** e suas ferramentas.  

Sinto-me confiante de que, com mais prática e estudos, poderei aprimorar ainda mais este projeto e enfrentar desafios maiores no futuro.
