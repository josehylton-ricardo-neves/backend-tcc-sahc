package br.edu.ifpb.sahc.dto.horario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Horarios {

	private String horaInicial;
	private String horaFinal;
}
