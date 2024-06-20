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

/**
 * Контроллер для управления организациями.
 */
@RestController
@RequestMapping("api/organization") // Базовый URL для всех операций с организациями
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;

    //#region Получение данных организаций
    /**
     * Получение списка всех организаций.
     *
     * @return Список организаций.
     */
    @GetMapping("/")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.findAll();
        return ResponseEntity.ok(organizations);
    }
    /**
     * Получение организации по её ID.
     *
     * @param id ID организации.
     * @return Организация, если найдена.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return organizationService.findById(id)
                .map(organization -> ResponseEntity.ok(organization))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));
    }
    /**
     * Получение наименования организации по её ID.
     *
     * @param organizationId ID организации.
     * @return Наименование организации, если найдена.
     */
    @GetMapping("/{organizationId}/name")
    public ResponseEntity<String> getOrganizationName(@PathVariable Long organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isPresent()) {
            return ResponseEntity.ok(organization.get().getName());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Поиск организаций по наименованию и/или ИНН.
     *
     * @param name Наименование организации.
     * @param inn  ИНН организации.
     * @return Список подходящих организаций.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Organization>> searchOrganizations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String inn) {
        List<Organization> organizations = organizationService.searchByNameOrInn(name, inn);
        return ResponseEntity.ok(organizations);
    }
//#endregion

    //#region Регистрация и аутентификация

    /**
     * Регистрация новой организации.
     *
     * @param organization Данные организации.
     * @return Зарегистрированная организация.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@RequestBody Organization organization) {
        Organization savedOrganization = organizationService.registerOrganization(organization);
        return ResponseEntity.ok(savedOrganization);
    }
    /**
     * Аутентификация организации.
     *
     * @param inn      ИНН организации.
     * @param password Пароль организации.
     * @return Результат аутентификации.
     */
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

    //#endregion

    //#region Обновление и удаление данных организации

    /**
     * Обновление данных организации.
     *
     * @param id                ID организации.
     * @param organizationDetails Обновленные данные организации.
     * @return Обновленная организация.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(@PathVariable Long id, @RequestBody Organization organizationDetails) {
        Organization updatedOrganization = organizationService.updateOrganization(id, organizationDetails);
        return ResponseEntity.ok(updatedOrganization);
    }

    /**
     * Удаление организации по её ID.
     *
     * @param id ID организации.
     * @return Ответ с результатом удаления.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok().build();
    }
    //#endregion
}
