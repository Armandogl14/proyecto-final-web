package org.example.clases;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

@Entity("URL")
public class URL {
    @Id
    private ObjectId id;
    private String urlViejo;
    private String urlNuevo;
    private String usuario;
    private boolean activo;
    private int clicks;

    public URL(String urlViejo, String urlNuevo, String usuario, boolean activo) {
        this.urlViejo = urlViejo;
        this.urlNuevo = urlNuevo;
        this.usuario = usuario;
        this.activo = activo;
        this.clicks = 0;
    }

    public URL(ObjectId id, String urlViejo, String urlNuevo, String usuario, boolean activo, int clicks) {
        this.id = id;
        this.urlViejo = urlViejo;
        this.urlNuevo = urlNuevo;
        this.usuario = usuario;
        this.activo = activo;
        this.clicks = clicks;
    }

    public URL() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUrlViejo() {
        return urlViejo;
    }

    public void setUrlViejo(String urlViejo) {
        this.urlViejo = urlViejo;
    }

    public String getUrlNuevo() {
        return urlNuevo;
    }

    public void setUrlNuevo(String urlNuevo) {
        this.urlNuevo = urlNuevo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}
