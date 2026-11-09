package com.davinci.crewup;

public class Plan {

    private final int id;
    private final String titulo;
    private final String descripcion;
    private final String categoria;
    private final String ubicacion;
    private final String puntoEncuentro;
    private final String fechaHora;
    private int participantes;
    private final int capacidad;
    private final int costo; // 0 = gratis
    private final String organizador;
    private final double ratingOrganizador;
    private final int imagenRes;
    private EstadoSolicitud estadoSolicitud = EstadoSolicitud.NINGUNA;

    public Plan(int id, String titulo, String descripcion, String categoria,
                String ubicacion, String puntoEncuentro, String fechaHora,
                int participantes, int capacidad, int costo,
                String organizador, double ratingOrganizador, int imagenRes) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.puntoEncuentro = puntoEncuentro;
        this.fechaHora = fechaHora;
        this.participantes = participantes;
        this.capacidad = capacidad;
        this.costo = costo;
        this.organizador = organizador;
        this.ratingOrganizador = ratingOrganizador;
        this.imagenRes = imagenRes;
    }

    public boolean esGratis() {
        return costo == 0;
    }

    public boolean estaCompleto() {
        return participantes >= capacidad;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public String getUbicacion() { return ubicacion; }
    public String getPuntoEncuentro() { return puntoEncuentro; }
    public String getFechaHora() { return fechaHora; }
    public int getParticipantes() { return participantes; }
    public int getCapacidad() { return capacidad; }
    public int getCosto() { return costo; }
    public String getOrganizador() { return organizador; }
    public double getRatingOrganizador() { return ratingOrganizador; }
    public int getImagenRes() { return imagenRes; }
    public EstadoSolicitud getEstadoSolicitud() { return estadoSolicitud; }

    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }
    public boolean tieneSolicitudPendiente() {
        return estadoSolicitud == EstadoSolicitud.PENDIENTE;
    }

    // Envía o cancela la solicitud. No se puede pedir unirse a un plan completo.
    public void alternarSolicitud() {
        if (tieneSolicitudPendiente()) {
            estadoSolicitud = EstadoSolicitud.NINGUNA;
        } else if (!estaCompleto()) {
            estadoSolicitud = EstadoSolicitud.PENDIENTE;
        }
    }
}