package umg.progra2.Model;

public class Respuesta {
    private int id;
    private String seccion;
    private long telegramId;
    private int preguntaId;
    private String respuestaTexto;

    public Respuesta() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(long telegramId) {
        this.telegramId = telegramId;
    }

    public int getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(int preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }



    public Respuesta(String seccion, long telegramId, int preguntaId, String respuestaTexto) {
        this.seccion = seccion;
        this.telegramId = telegramId;
        this.preguntaId = preguntaId;
        this.respuestaTexto = respuestaTexto;
    }

}
