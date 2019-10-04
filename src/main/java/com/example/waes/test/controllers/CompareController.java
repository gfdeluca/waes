package com.example.waes.test.controllers;

import com.example.waes.test.api.Response;
import com.example.waes.test.models.SideEnum;
import com.example.waes.test.services.ComparisonService;
import com.example.waes.test.utils.KeyUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/v1/diff")
@Api(value = "Comparison", description = "API for comparing 2 binary files and check their differences")
public class CompareController {
    private ComparisonService comparisonService;

    @Autowired
    public CompareController(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }

    @GetMapping(value = "/key")
    @ApiOperation(value = "Creates a random key for comparing data", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Key created OK")
    })
    public ResponseEntity createKey() {
        return ResponseEntity.ok(KeyUtils.createKey());
    }

    @PutMapping(value = "/{id}/left", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add left data for the provided ID to be compared", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data added OK"),
            @ApiResponse(code = 409, message = "Data is missing")
    })
    public ResponseEntity left(
            @ApiParam(value = "ID for the comparison", required = true) @PathVariable(value = "id", required = true) String id,
            @ApiParam(value = "Data to be added to the comparison", required = true) @RequestParam("data") String data) throws Exception {
        comparisonService.addData(id, data, SideEnum.LEFT);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/right", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add right data for the provided ID to be compared", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data added OK"),
            @ApiResponse(code = 409, message = "Data is missing")
    })
    public ResponseEntity right(
            @ApiParam(value = "ID for the comparison", required = true) @PathVariable("id") String id,
            @ApiParam(value = "Data to be added to the comparison", required = true) @RequestParam("data") String data) {
        comparisonService.addData(id, data, SideEnum.RIGHT);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Compare the data associated to the provided ID", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comparison result"),
            @ApiResponse(code = 400, message = "Validation failure")
    })
    public ResponseEntity compare(
            @ApiParam(value = "ID for the comparison", required = true) @PathVariable("id") String id) {
        Response response = comparisonService.compare(id);
        return ResponseEntity.ok(response);
    }
}