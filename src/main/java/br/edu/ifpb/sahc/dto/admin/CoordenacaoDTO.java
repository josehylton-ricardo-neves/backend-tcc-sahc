package br.edu.ifpb.sahc.dto.admin;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CoordenacaoDTO {

    @NotBlank
    @Email
    private String emailCoordenacao;
    @NotBlank
    private String nomeCoordenacao;
    private long matriculaCoordenador;
    private String nomeCoordenador;
}