package org.example.windsurfmvc.mappers;

import org.example.windsurfmvc.dtos.BeerDto;
import org.example.windsurfmvc.entities.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for converting between Beer and BeerDto
 */

@Mapper(componentModel = "spring")
public interface BeerMapper {

    /**
     * Converts a Beer entity to a BeerDto
     *
     * @param beer the Beer entity to convert
     * @return the converted BeerDto
     */
    BeerDto beerToBeerDto(Beer beer);

    /**
     * Converts a BeerDto to a Beer entity, ignoring ID and timestamps
     *
     * @param beerDto the BeerDto to convert
     * @return the converted Beer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);

    /**
     * Updates a Beer entity from a BeerDto, ignoring ID and timestamps
     *
     * @param beerDto the source BeerDto
     * @param beer    the target Beer entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updateBeerFromDto(BeerDto beerDto, @MappingTarget Beer beer);
}
