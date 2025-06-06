package com.generation.ladydrive.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.ladydrive.model.Veiculo;
import com.generation.ladydrive.repository.VeiculoRepository;
import com.generation.ladydrive.repository.ViagemRepository;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/veiculos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VeiculoController {

	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private ViagemRepository viagemRepository;
	
	@GetMapping
	public ResponseEntity<List<Veiculo>> getAll(){
		return ResponseEntity.ok(veiculoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Veiculo> getById(@PathVariable Long id){
		return veiculoRepository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/modelo/{modelo}")
	public ResponseEntity<List<Veiculo>> getByTitulo(@PathVariable String modelo){
		return ResponseEntity.ok(veiculoRepository.findAllByModeloContainingIgnoreCase(modelo));
		
	}
	
	@PostMapping
    public ResponseEntity<Veiculo> post(@Valid @RequestBody Veiculo veiculo){
        if (viagemRepository.existsById(veiculo.getViagem().getId()))
            return ResponseEntity.status(HttpStatus.CREATED).
                    body(veiculoRepository.save(veiculo));

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Viagem não existe", null);
	} 
	
	@PutMapping
    public ResponseEntity<Veiculo> put(@Valid @RequestBody Veiculo veiculo){
        if(veiculoRepository.existsById(veiculo.getId())) {

            if(viagemRepository.existsById(veiculo.getViagem().getId()))
                return ResponseEntity.status(HttpStatus.OK).body(veiculoRepository.save(veiculo));

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Viagem não existe", null);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

	
	@ResponseStatus (HttpStatus.NO_CONTENT) 
	@DeleteMapping ("/{id}")
	
	public void delete(@PathVariable Long id) {
		Optional<Veiculo> veiculo = veiculoRepository.findById(id);
		
		if(veiculo.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
		
		veiculoRepository.deleteById(id);

	}
}

		
		
	
