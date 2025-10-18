package com.kraftbrains.mshexarqspb.infrastructure.web.controller;

import com.kraftbrains.mshexarqspb.domain.model.Endosso;
import com.kraftbrains.mshexarqspb.domain.port.in.EndossoUseCasePort;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.EndossoRequestDTO;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.EndossoResponseDTO;
import com.kraftbrains.mshexarqspb.infrastructure.web.mapper.EndossoDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para operações de Endosso
 */
@RestController
@RequestMapping("/api/v1/endossos")
@RequiredArgsConstructor
public class EndossoController {

    private final EndossoUseCasePort endossoUseCase;
    private final EndossoDTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<EndossoResponseDTO> criarEndosso(@RequestBody EndossoRequestDTO requestDTO) {
        Endosso endosso = dtoMapper.toDomain(requestDTO);
        Endosso endossoCriado = endossoUseCase.criarEndosso(endosso);
        EndossoResponseDTO responseDTO = dtoMapper.toResponseDTO(endossoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndossoResponseDTO> buscarPorId(@PathVariable UUID id) {
        Endosso endosso = endossoUseCase.buscarPorId(id);
        EndossoResponseDTO responseDTO = dtoMapper.toResponseDTO(endosso);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/titulo/{numeroTitulo}")
    public ResponseEntity<List<EndossoResponseDTO>> buscarPorNumeroTitulo(@PathVariable String numeroTitulo) {
        List<Endosso> endossos = endossoUseCase.buscarPorNumeroTitulo(numeroTitulo);
        List<EndossoResponseDTO> responseDTOs = endossos.stream()
                .map(dtoMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping
    public ResponseEntity<List<EndossoResponseDTO>> listarTodos() {
        List<Endosso> endossos = endossoUseCase.listarTodos();
        List<EndossoResponseDTO> responseDTOs = endossos.stream()
                .map(dtoMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<EndossoResponseDTO> aprovarEndosso(@PathVariable UUID id) {
        Endosso endosso = endossoUseCase.aprovarEndosso(id);
        EndossoResponseDTO responseDTO = dtoMapper.toResponseDTO(endosso);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<EndossoResponseDTO> cancelarEndosso(@PathVariable UUID id) {
        Endosso endosso = endossoUseCase.cancelarEndosso(id);
        EndossoResponseDTO responseDTO = dtoMapper.toResponseDTO(endosso);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndosso(@PathVariable UUID id) {
        endossoUseCase.deletarEndosso(id);
        return ResponseEntity.noContent().build();
    }
}

