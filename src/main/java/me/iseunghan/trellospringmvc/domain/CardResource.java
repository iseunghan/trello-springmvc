package me.iseunghan.trellospringmvc.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.iseunghan.trellospringmvc.controller.CardController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class CardResource extends RepresentationModel {

    @Autowired
    private ModelMapper modelMapper;

    @JsonUnwrapped
    private CardDto cardDto;

    public CardResource() {

    }

    public CardResource(CardDto cardDto) {
        super.add(linkTo(CardController.class, cardDto.getPocketId()).slash(cardDto.getCardId()).withSelfRel());
        this.cardDto = cardDto;
    }

    public CardResource toModel(Card card) {
        CardDto cardDto = modelMapper.map(card, CardDto.class);
        return new CardResource(cardDto);
    }

    public CollectionModel<CardResource> toCollectionModel(List<Card> cards) {
        List<CardResource> cardResources = new ArrayList<>();
        List<CardDto> cardDtoList = cards
                .stream()
                .map(card -> modelMapper.map(card, CardDto.class))
                .collect(Collectors.toList());
        cardDtoList
                .stream()
                .forEach(c -> cardResources.add(new CardResource(c)));
        CollectionModel<CardResource> collectionModel = CollectionModel.of(cardResources);
        Link link = linkTo(CardController.class, cardDtoList.get(0).getPocketId()).withSelfRel();
        collectionModel.add(link);

        return collectionModel;
    }

    public CardDto getCardDto() {
        return cardDto;
    }
}
