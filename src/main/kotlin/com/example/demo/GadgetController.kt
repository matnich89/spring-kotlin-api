package com.example.demo

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class GadgetController(private val repo: GadgetRepo) {

    @GetMapping("/gadgets")
    fun fetchGadgets(): ResponseEntity<List<GadgetDto>> {
        val gadgets = repo.findAll()
        if (gadgets.isEmpty()) {
            return ResponseEntity<List<GadgetDto>>(HttpStatus.NO_CONTENT)
        }
        val dtos = mutableListOf<GadgetDto>()
        gadgets.mapTo(dtos) { it.toDto() }
        return ResponseEntity<List<GadgetDto>>(dtos, HttpStatus.OK)
    }

    @GetMapping("/gadgets/{id}")
    fun fetchGadgetById(@PathVariable("id") gadgetId: Long): ResponseEntity<Gadget> {
        val gadget = repo.findById(gadgetId)
        if (gadget.isPresent) {
            return ResponseEntity<Gadget>(gadget.get(), HttpStatus.OK)
        }
        return ResponseEntity<Gadget>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/gadgets")
    fun addNewGadget(@RequestBody gadget: GadgetDto, uri: UriComponentsBuilder): ResponseEntity<Gadget> {
        val persistedGadget = repo.save(gadget.toEntity())
        if (ObjectUtils.isEmpty(persistedGadget)) {
            return ResponseEntity<Gadget>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()

        headers.location = uri.path("/gadget/{gadgetId").buildAndExpand(gadget.gadgetId).toUri()
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/gadgets/{id}")
    fun updateGadgetById(@PathVariable("id") gadgetId: Long, @RequestBody gadget: Gadget): ResponseEntity<Gadget> {
        return repo.findById(gadgetId).map { gadgetDetails ->
            val updatedGadget: Gadget = gadgetDetails.copy(
                gadgetCategory = gadget.gadgetCategory,
                gadgetName = gadget.gadgetName,
                gadgetPrice = gadget.gadgetPrice,
                gadgetAvailability = gadget.gadgetAvailability
            )
            ResponseEntity(repo.save(updatedGadget), HttpStatus.OK)
        }.orElse(ResponseEntity<Gadget>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/gadgets/{id}")
    fun removeGadgetById(@PathVariable("id") gadgetId: Long): ResponseEntity<Void> {
         val gadget = repo.findById(gadgetId)
        if (gadget.isPresent) {
            repo.deleteById(gadgetId)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
