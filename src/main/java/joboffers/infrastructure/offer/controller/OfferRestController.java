package joboffers.infrastructure.offer.controller;

import joboffers.domain.offer.OfferFacade;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.infrastructure.offer.controller.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/offers")
public class OfferRestController {

    private final OfferFacade offerFacade;
    @GetMapping
    public ResponseEntity<UserResponseDto> findAlloffers() {
        final List<OfferResponseDto> offerResponseDtos = offerFacade.findAllOffers();
        return ResponseEntity.ok(UserResponseDto.builder()
                .offers(offerResponseDtos)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDto> findOfferById(@PathVariable String id) {
        return ResponseEntity.ok(offerFacade.findOfferById(id));
    }
}
