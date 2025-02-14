package com.rabbit.consumidor.service.Impl;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.consumidor.dto.AlertaDTO;
import com.rabbit.consumidor.model.Alerta;
import com.rabbit.consumidor.model.Paciente;
import com.rabbit.consumidor.model.SignosVitales;
import com.rabbit.consumidor.repository.AlertaRepository;
import com.rabbit.consumidor.repository.PacienteRepository;
import com.rabbit.consumidor.repository.SignosVitalesRepository;
import com.rabbit.consumidor.service.AlertaService;

@Service
public class AlertaServiceImpl implements AlertaService{

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private SignosVitalesRepository signosVitalesRepository;


    @Override
    @RabbitListener(queues = "myQueue")
    public void guardarAlerta(AlertaDTO alertaDTO) {
        Alerta alerta = new Alerta();
        alerta.setMensaje(alertaDTO.getMensaje());
        alerta.setTipo(alertaDTO.getTipo());
        alerta.setAtendida(alertaDTO.getAtendida());
        alerta.setSeveridad(alertaDTO.getSeveridad());
        alerta.setFechaGeneracion(alertaDTO.getFechaGeneracion());
        alerta.setSource("rabbitMQ");

        // Buscar el paciente por ID
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(alertaDTO.getPacienteId());
        if (pacienteOpt.isPresent()) {
            alerta.setPaciente(pacienteOpt.get());
        } else {
            System.out.println("Paciente con ID " + alertaDTO.getPacienteId() + " no encontrado.");
            return; // No guardar si el paciente no existe
        }

        // Buscar signos vitales por ID (si está presente)
        if (alertaDTO.getSignoVitalId() != null) {
            Optional<SignosVitales> signosVitalesOpt = signosVitalesRepository.findById(alertaDTO.getSignoVitalId());
            signosVitalesOpt.ifPresent(alerta::setSignosVitales);
        }else{
            System.out.println("Signos vitales con ID " + alertaDTO.getSignoVitalId() + " no encontrado.");
            return; // No guardar si los signos vitales no existen.
        }

        alertaRepository.save(alerta);
        System.out.println("Alerta guardada en la base de datos: " + alerta.getMensaje());
    }
    
}
