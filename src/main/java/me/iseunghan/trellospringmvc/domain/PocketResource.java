package me.iseunghan.trellospringmvc.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.iseunghan.trellospringmvc.controller.PocketController;
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
public class PocketResource extends RepresentationModel {

    @JsonUnwrapped
    private PocketDto pocketDto;

    @Autowired
    private ModelMapper modelMapper;

    public PocketResource() {

    }

    public PocketResource(PocketDto pocketDto) {
        super.add(linkTo(PocketController.class, pocketDto.getBoardId()).slash(pocketDto.getPocketId()).withSelfRel());
        this.pocketDto = pocketDto;
    }

    public PocketResource toModel(Pocket pocket) {
        PocketDto pocketDto = modelMapper.map(pocket, PocketDto.class);
        return new PocketResource(pocketDto);
    }

    public CollectionModel<PocketResource> toCollectionModel(List<Pocket> pockets) {
        List<PocketResource> pocketResources = new ArrayList<>();
        List<PocketDto> pocketDtoList = pockets
                .stream()
                .map(pocket -> modelMapper.map(pocket, PocketDto.class))
                .collect(Collectors.toList());
        pocketDtoList
                .stream()
                .forEach(p -> pocketResources.add(new PocketResource(p)));

        CollectionModel<PocketResource> collectionModel = CollectionModel.of(pocketResources);
        Link link = linkTo(PocketController.class, pocketDtoList.get(0).getBoardId()).withSelfRel();
        collectionModel.add(link);
        return collectionModel;
    }

    public PocketDto getPocketDto() {
        return pocketDto;
    }
}
