package org.example.clases;

public class URL {
    private int id;
    private String urlViejo;
    private String urlNuevo;
    private Usuario usuario;
    private boolean activo;
    private int clicks;

    public URL(int id, String urlViejo, String urlNuevo, Usuario usuario, boolean activo) {
        this.id = id;
        this.urlViejo = urlViejo;
        this.urlNuevo = urlNuevo;
        this.usuario = usuario;
        this.activo = activo;
        this.clicks = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
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
