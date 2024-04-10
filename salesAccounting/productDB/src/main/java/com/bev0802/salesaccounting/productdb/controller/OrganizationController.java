package com.bev0802.salesaccounting.productdb.controller;

import com.bev0802.salesaccounting.productdb.model.Organization;
import com.bev0802.salesaccounting.productdb.repository.OrganizationRepository;
import com.bev0802.salesaccounting.productdb.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/organizations") // Базовый URL для всех операций с организациями
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;

    // Метод для получения списка всех организаций
    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.findAll();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return organizationService.findById(id)
                .map(organization -> ResponseEntity.ok(organization))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));
    }

    @GetMapping("/{organizationId}/name")
    public ResponseEntity<String> getOrganizationName(@PathVariable Long organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isPresent()) {
            return ResponseEntity.ok(organization.get().getName());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Метод для поиска организаций по наименованию и/или ИНН
    @GetMapping("/search")
    public ResponseEntity<List<Organization>> searchOrganizations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String inn) {
        List<Organization> organizations = organizationService.searchByNameOrInn(name, inn);
        return ResponseEntity.ok(organizations);
    }

    // Метод для регистрации новой организации
    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@RequestBody Organization organization) {
        Organization savedOrganization = organizationService.registerOrganization(organization);
        return ResponseEntity.ok(savedOrganization);
    }

    // Метод для аутентификации организации
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateOrganization(@RequestParam String inn, @RequestParam String password) {

        Optional<Organization> organization = organizationRepository.findByInnAndPassword(inn, password);
        if (organization.isPresent()) {
            // Сюда можно добавить логику создания и возврата токена аутентификации
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Аутентификация успешна");
            // Допустим, возвращаем ID организации, который может быть использован для дальнейших запросов
            response.put("organizationId", organization.get().getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Организация не найдена или неверный пароль");
        }
    }

    // Метод для обновления данных организации
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(@PathVariable Long id, @RequestBody Organization organizationDetails) {
        Organization updatedOrganization = organizationService.updateOrganization(id, organizationDetails);
        return ResponseEntity.ok(updatedOrganization);
    }

    // Метод для удаления организации по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok().build();
    }
}
