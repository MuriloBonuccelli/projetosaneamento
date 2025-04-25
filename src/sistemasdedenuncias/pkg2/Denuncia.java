/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemasdedenuncias.pkg2;

/**
 *
 * @author Muril
 */
class Denuncia {
    int id;
    private String descricao;
private String local;
    private boolean resolvida;

    public Denuncia(int id, String descricao, String local, boolean resolvida) {
        this.descricao = descricao;
        this.local = local;
        this.resolvida = false;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocal() {
        return local;
    }

    public boolean isResolvida() {
        return resolvida;
    }

    public void resolver() {
        this.resolvida = true;
    }
}
    

