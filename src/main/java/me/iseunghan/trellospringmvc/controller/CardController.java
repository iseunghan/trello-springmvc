package me.iseunghan.trellospringmvc.controller;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Card;
import me.iseunghan.trellospringmvc.domain.CardDto;
import me.iseunghan.trellospringmvc.domain.CardResource;
import me.iseunghan.trellospringmvc.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/pockets/{pocketId}/cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private CardResource cardResource;

    @GetMapping
    public ResponseEntity list(@PathVariable Long pocketId) {
        List<Card> cards = cardService.findAll(pocketId);
        CollectionModel<CardResource> collectionModel = cardResource.toCollectionModel(cards);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{cardId}")
    public ResponseEntity findOne(@PathVariable Long cardId) throws NotFoundException {
        Card card = cardService.findOne(cardId);
        CardResource resource = cardResource.toModel(card);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity addCard(@PathVariable Long pocketId, @RequestBody CardDto cardDto) throws NotFoundException {
        Long id = cardService.addCard(pocketId, cardDto);
        Card card = cardService.findOne(id);
        CardResource resource = cardResource.toModel(card);
        URI uri = linkTo(CardController.class, pocketId).slash(id).withSelfRel().toUri();
        return ResponseEntity.created(uri).body(resource);
    }

    @PatchMapping(value = "/{cardId}")
    public ResponseEntity update(@PathVariable Long pocketId, @PathVariable Long cardId, @RequestBody CardDto cardDto) throws NotFoundException {
        Card card = cardService.updateCard(cardId, cardDto);
        CardResource resource = cardResource.toModel(card);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping(value = "/{cardId}")
    public ResponseEntity delete(@PathVariable Long pocketId, @PathVariable Long cardId) throws NotFoundException {
        boolean result = cardService.deleteCard(cardId);
        return ResponseEntity.ok(result);
    }
}
