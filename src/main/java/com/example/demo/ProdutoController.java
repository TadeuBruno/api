package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    public final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }
    @GetMapping("/selecionar")
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }

    @GetMapping("/selecionar/{id}")
    public ResponseEntity<Produto> getProdutoPorId(@PathVariable Long id){
     if(produtoRepository.existsById(id)) {
         Produto produto = produtoRepository.findById(id).orElse(null);
         return ResponseEntity.ok(produto);

     }else{
            return ResponseEntity.notFound().build();
     }
    }


    @PostMapping("/inserir")
    public ResponseEntity<String> inserirProduto(@RequestBody Produto produto){
        produtoRepository.save(produto);
        return ResponseEntity.ok("Produto inserido com sucesso");
    }

    @PostMapping("/inserirForm")
    public ResponseEntity<String> inserirProduto(@RequestParam String nome, @RequestParam String descricao, @RequestParam double preco,@RequestParam int quantidade){
        Produto novoProduto = new Produto(nome, descricao, preco, quantidade);

        try{
            produtoRepository.save(novoProduto);
            return ResponseEntity.ok("Produto inserido");
        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirProduto(@PathVariable Long id){
        produtoRepository.deleteById(id);
        return ResponseEntity.ok("Excluido com sucesso");
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado){
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if(produtoExistente.isPresent()){
            Produto produto = produtoExistente.get();
            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQuantidadeestoque(produtoAtualizado.getQuantidadeestoque());
            produtoRepository.save(produto);
            return ResponseEntity.ok("Produto Alterado");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
