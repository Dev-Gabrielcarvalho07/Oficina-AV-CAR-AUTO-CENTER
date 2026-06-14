package br.com.avcar.model;

// Padrão: GENERICS — permite que subclasses declarem o tipo do ID
public abstract class BaseModel<ID> {

    protected ID id;

    public ID getId() { return id; }
    public void setId(ID id) { this.id = id; }
}
