package com.seibe.docente.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocenteDTO {
    private Long docenteId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String docNombre;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]{10}$", message = "La identificación debe tener 10 dígitos")
    private String docIdentificacion;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 100, message = "El correo no puede tener más de 100 caracteres")
    private String docCorreo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    @Size(max = 10, message = "El teléfono debe tener 10 dígitos")
    private String docTelefono;

    @NotNull(message = "El estado es obligatorio")
    private Boolean docEstado;

    private Long institucionId;
    
    private Long areaId;

    // Métodos de compatibilidad para mantener código existente
    @JsonIgnore
    public String getNombre() {
        return this.docNombre;
    }
    
    @JsonIgnore
    public void setNombre(String nombre) {
        this.docNombre = nombre;
    }
    
    @JsonIgnore
    public String getIdentificacion() {
        return this.docIdentificacion;
    }
    
    @JsonIgnore
    public void setIdentificacion(String identificacion) {
        this.docIdentificacion = identificacion;
    }
    
    @JsonIgnore
    public String getCorreo() {
        return this.docCorreo;
    }
    
    @JsonIgnore
    public void setCorreo(String correo) {
        this.docCorreo = correo;
    }
    
    @JsonIgnore
    public String getTelefono() {
        return this.docTelefono;
    }
    
    @JsonIgnore
    public void setTelefono(String telefono) {
        this.docTelefono = telefono;
    }
    
    @JsonIgnore
    public Boolean getEstado() {
        return this.docEstado;
    }
    
    @JsonIgnore
    public void setEstado(Boolean estado) {
        this.docEstado = estado;
    }
} 