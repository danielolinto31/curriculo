package br.com.curriculo.arq.commons.utils;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.curriculo.arq.persistence.Persistent;

/**
 * Converter responsável por permitir a manipulação de SelectOneMenu
 * (ou SelectManyMenu) e entidades em JSF, de forma que o value do SelectItem
 * seja a própria entidade e não o "id" da mesma.
 *
 * Para isso armazenamos as entidades como atributos do nosso componente no
 * método getAsString() e recuperamos a entidade correta através da
 * chave (neste caso o id) associada a ela, no método getAsObject().
 *
 */
@FacesConverter("br.com.curriculo.arq.commons.utils.SimpleEntity")
public class SimpleEntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        if (value != null) {
            return this.getAttributesFrom(component).get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {
        if (value != null && !"".equals(value)) {
            if (!(value instanceof Persistent)) {
                throw new RuntimeException(
                        String.format(
                                "%s só pode ser utilizado com entidades do tipo %s, o que não é o caso da entidade %s.",
                                SimpleEntityConverter.class.getName(),
                                Persistent.class.getName(), value.getClass()
                                        .getName()));
            }            
            
            Persistent entity = (Persistent) value;
                        
            if (entity.getId() != null) {
                // Adiciona item como atributo do componente.
                this.addAttribute(component, entity);
                
                Integer id = entity.getId();
                if (id != null) {
                    return String.valueOf(id);
                }
            }
        }

        return null;
    }

    protected void addAttribute(UIComponent component, Persistent persistent) {
        String key = Integer.toString(persistent.getId());
        this.getAttributesFrom(component).put(key, persistent);
    }

    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }

}
