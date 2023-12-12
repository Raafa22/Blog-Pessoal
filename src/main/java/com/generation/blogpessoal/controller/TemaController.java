package com.generation.blogpessoal.controller;

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

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {
    
    @Autowired
    private TemaRepository temaRepository;
    
    @GetMapping
    public ResponseEntity<List<Tema>> getAll(){
        return ResponseEntity.ok(temaRepository.findAll());
    }
    
    @GetMapping("/{id}") //puxar itens pelo id 
    public ResponseEntity<Tema> getById(@PathVariable Long id){
        return temaRepository.findById(id)
            .map(resposta -> ResponseEntity.ok(resposta)) // achar pelo id puxar 
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // nao achar pelo id dar mensagem de erro 
    }
    @GetMapping("descricao/{descricao}")//buscar por descricao 
    public ResponseEntity<List<Tema>>getBytitle(@PathVariable String descricao){
    return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
    }
    
    @PostMapping  // esse item é para a criacao de temas 
    public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(temaRepository.save(tema));
    }
    
    @PutMapping //atualizar tema 
    public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema){
        return temaRepository.findById(tema.getId())
            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)//achar tema retornar ele 
            .body(temaRepository.save(tema))) //salvar criacao de tema 
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); //nao achar mensagem de erro      
        
    }
    //deletar algum tema 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { 
        Optional<Tema> tema = temaRepository.findById(id);
        
        if(tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);//se nao achar mensagem de erro 
        
        temaRepository.deleteById(id);//se achar deletar               
    }

}
  
   