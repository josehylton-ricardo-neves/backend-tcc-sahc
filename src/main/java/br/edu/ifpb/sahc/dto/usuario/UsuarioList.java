package br.edu.ifpb.sahc.dto.usuario;

import br.edu.ifpb.sahc.model.Coordenacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioList implements Serializable {


    private static final long serialVersionUID = 1L;

    private String matricula;
    private String nome;
    private String email;
    private String role;
    private Coordenacao idCoordenacao;

}