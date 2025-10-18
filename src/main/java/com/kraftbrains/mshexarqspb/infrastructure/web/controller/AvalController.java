package com.kraftbrains.mshexarqspb.infrastructure.web.controller;

import com.kraftbrains.mshexarqspb.domain.model.Aval;
import com.kraftbrains.mshexarqspb.domain.port.in.AvalUseCasePort;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.AvalRequestDTO;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.AvalResponseDTO;
import com.kraftbrains.mshexarqspb.infrastructure.web.mapper.AvalDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para operações de Aval
 */
@RestController
@RequestMapping("/api/v1/avais")
@RequiredArgsConstructor
public class AvalController {

    private final AvalUseCasePort avalUseCase;
    private final AvalDTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<AvalResponseDTO> criarAval(@RequestBody AvalRequestDTO requestDTO) {
        Aval aval = dtoMapper.toDomain(requestDTO);
        Aval avalCriado = avalUseCase.criarAval(aval);
        AvalResponseDTO responseDTO = dtoMapper.toResponseDTO(avalCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvalResponseDTO> buscarPorId(@PathVariable UUID id) {
        Aval aval = avalUseCase.buscarPorId(id);
        AvalResponseDTO responseDTO = dtoMapper.toResponseDTO(aval);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/titulo/{numeroTitulo}")
    public ResponseEntity<List<AvalResponseDTO>> buscarPorNumeroTitulo(@PathVariable String numeroTitulo) {
        List<Aval> avais = avalUseCase.buscarPorNumeroTitulo(numeroTitulo);
        List<AvalResponseDTO> responseDTOs = avais.stream()
                .map(dtoMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping
    public ResponseEntity<List<AvalResponseDTO>> listarTodos() {
        List<Aval> avais = avalUseCase.listarTodos();
        List<AvalResponseDTO> responseDTOs = avais.stream()
                .map(dtoMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<AvalResponseDTO> aprovarAval(@PathVariable UUID id) {
        Aval aval = avalUseCase.aprovarAval(id);
        AvalResponseDTO responseDTO = dtoMapper.toResponseDTO(aval);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/executar")
    public ResponseEntity<AvalResponseDTO> executarAval(@PathVariable UUID id) {
        Aval aval = avalUseCase.executarAval(id);
        AvalResponseDTO responseDTO = dtoMapper.toResponseDTO(aval);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<AvalResponseDTO> cancelarAval(@PathVariable UUID id) {
        Aval aval = avalUseCase.cancelarAval(id);
        AvalResponseDTO responseDTO = dtoMapper.toResponseDTO(aval);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAval(@PathVariable UUID id) {
        avalUseCase.deletarAval(id);
        return ResponseEntity.noContent().build();
    }
}

