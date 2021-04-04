package me.iseunghan.trellospringmvc.controller;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Pocket;
import me.iseunghan.trellospringmvc.domain.PocketDto;
import me.iseunghan.trellospringmvc.domain.PocketResource;
import me.iseunghan.trellospringmvc.service.PocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/boards/{boardId}/pockets", produces = MediaType.APPLICATION_JSON_VALUE)
public class PocketController {

    @Autowired
    private PocketService pocketService;
    @Autowired
    private PocketResource pocketResource;

    @GetMapping
    public ResponseEntity list(@PathVariable Long boardId) {
        List<Pocket> pockets = pocketService.findAll(boardId);
        if (!pockets.isEmpty()){
            CollectionModel<PocketResource> pocketResources = pocketResource.toCollectionModel(pockets);
            return ResponseEntity.ok(pocketResources);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{pocketId}")
    public ResponseEntity findOne(@PathVariable Long boardId, @PathVariable Long pocketId) throws NotFoundException {
        Pocket pocket = pocketService.findOne(pocketId);
        PocketResource resource = pocketResource.toModel(pocket);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity addPocket(@PathVariable Long boardId, @RequestBody PocketDto pocketDto) throws NotFoundException {
        System.out.println("boardId :  " + boardId);
        Long id = pocketService.addPocket(boardId, pocketDto);
        Pocket pocket = pocketService.findOne(id);
        PocketResource resource = pocketResource.toModel(pocket);
        URI uri = linkTo(PocketController.class, boardId).slash(pocket.getId()).withSelfRel().toUri();
        return ResponseEntity.created(uri).body(resource);
    }

    @PatchMapping("/{pocketId}")
    public ResponseEntity update(@PathVariable Long boardId, @PathVariable Long pocketId, @RequestBody PocketDto pocketDto) throws NotFoundException {
        Pocket pocket = pocketService.updatePocket(pocketId, pocketDto);
        PocketResource resource = pocketResource.toModel(pocket);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("{pocketId}")
    public ResponseEntity delete(@PathVariable Long boardId, @PathVariable Long pocketId) throws NotFoundException {
        boolean result = pocketService.deletePocket(boardId, pocketId);
        return ResponseEntity.ok(result);
    }
}
