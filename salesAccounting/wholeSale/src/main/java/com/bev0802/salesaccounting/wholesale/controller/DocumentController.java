package com.bev0802.salesaccounting.wholesale.controller;

import com.bev0802.salesaccounting.wholesale.model.Document;
import com.bev0802.salesaccounting.wholesale.model.DocumentItem;
import com.bev0802.salesaccounting.wholesale.service.DocumentService;
import com.bev0802.salesaccounting.wholesale.service.OrderService;
import com.bev0802.salesaccounting.wholesale.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("organization/{organizationId}/employee/{employeeId}/documents/")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private OrganizationService organizationService;

    /**
     * Получает список документов на основе идентификаторов организации и сотрудников.
     *
     * @param organizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @param source Необязательный параметр для фильтрации документов по источнику.
     * @param model Модель для добавления атрибутов для отображения в шаблоне.
     * @return Имя представления для списка документов.
     */
    @GetMapping("/sellerList")
    public String getDocumentsBySeller(@PathVariable Long organizationId,
                                       @PathVariable Long employeeId,
                                       @RequestParam(required = false) String source,
                                       Model model) {
        //Получение списка документов, по идентификатору организации продавца и сотрудника
        List<Document> documents = documentService.findDocumentsBySellerId(organizationId, employeeId);
        documents.sort(Comparator.comparing(Document::getId).reversed());

        //Добавление данных в модель для отображения в шаблоне
        model.addAttribute("documents", documents);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
        logger.info("Documents for organization {} and employee {} retrieved: {}", organizationId, employeeId, documents);
        return "listDocuments";
    }
    /**
     * Получает список документов на основе идентификаторов организации и сотрудников.
     *
     * @param organizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @param source Необязательный параметр для фильтрации документов по источнику.
     * @param model Модель для добавления атрибутов для рендеринга шаблона.
     * @return Имя представления для отображения списка документов.
     */
    @GetMapping("/buyerList")
    public String getDocumentsByBuyer(@PathVariable Long organizationId,
                                      @PathVariable Long employeeId,
                                      @RequestParam(required = false) String source,
                                      Model model) {
        //Получение списка документов, по идентификатору организации покупателя и сотрудника
        List<Document> documents = documentService.findDocumentsByBuyerId(organizationId, employeeId);
        documents.sort(Comparator.comparing(Document::getId).reversed());
        //Добавление данных в модель для отображения в шаблоне
        model.addAttribute("documents", documents);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
        logger.info("Documents for organization {} and employee {} retrieved: {}", organizationId, employeeId, documents);
        return "listDocuments";
    }
    /**
     * Получить документ по его идентификатору.
     *
     * @param organizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @param documentId Идентификатор документа, который требуется получить.
     * @param source Источник документа (необязательно).
     * @param model Модель для добавления атрибутов для отрисовки шаблона.
     * @return Имя шаблона для отображения сведений о документе.
     */
    @GetMapping("/{documentId}")
    public String getDocumentById(@PathVariable Long organizationId,
                                  @PathVariable Long employeeId,
                                  @PathVariable Long documentId,
                                  @RequestParam(required = false) String source,
                                  Model model) {
        //Получение документа по идентификатору
        Document document = documentService.findById(documentId, organizationId, employeeId);

        //Добавление данных в модель для отображения в шаблоне
        model.addAttribute("document", document);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
        logger.info("Document {} for organization {} and employee {} retrieved: {}", documentId, organizationId, employeeId, document);
        return "detailDocument";
    }

    /**
     * Отображение списка купленных товаров по идентификатору организации и сотрудника.
     * @param organizationId - идентификатор организации покупателя
     * @param employeeId - идентификатор сотрудника организации покупателя
     * @param model - модель для добавления атрибутов для отображения шаблона
     * @return форму listReport с данными проданных(купленных) товаров (наименование, количество, общая сумма продажи)
     */
    @GetMapping("/reportBuy")
    public String getDocumentItemReportByBuyer(@PathVariable Long organizationId,
                                               @PathVariable Long employeeId,
                                               Model model) {
        List<DocumentItem> documentItems = documentService.findDocumentItemsByBuyer(organizationId, employeeId);
        //Сортировка по наименованию
        Collections.sort(documentItems, Comparator.comparing(item -> item.getProduct().getName()));
        //Получение итоговой суммы строк
        BigDecimal totalAmount = documentItems.stream()
                .map(DocumentItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("documentItems", documentItems);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
        model.addAttribute("totalAmount", totalAmount);

        return "listReport";
    }
    /**
     * Отображение списка проданных товаров по идентификатору организации и сотрудника.
     * @param organizationId - идентификатор организации продавца
     * @param employeeId
     * @param source
     * @param model
     * @return форму listReport с данными проданных(купленных) товаров (наименование, количество, общая сумма продажи)
     */
    @GetMapping("/reportSale")
    public String getDocumentItemReportBySeller(@PathVariable Long organizationId,
                                        @PathVariable Long employeeId,
                                        @RequestParam(required = false) String source,
                                        Model model) {
        List<DocumentItem> documentItems = documentService.findDocumentItemsBySeller(organizationId, employeeId);

        Collections.sort(documentItems, Comparator.comparing(item -> item.getProduct().getName()));

        BigDecimal totalAmount = documentItems.stream()
                .map(DocumentItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("documentItems", documentItems);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
        model.addAttribute("totalAmount", totalAmount);

        return "listReport";
    }


    /**
     * Отображение данных отчета ввиде диаграммы.
     *
     *
     */

    @GetMapping("/chartReport")
    public String getChartReport(@PathVariable Long organizationId,
                                 @PathVariable Long employeeId,
                                 Model model) {
        List<DocumentItem> documentItems = documentService.findDocumentItemsByBuyer(organizationId, employeeId);

        // Сортируем товары по наименованию
        documentItems.sort(Comparator.comparing(item -> item.getProduct().getName()));

        model.addAttribute("documentItems", documentItems);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));

        return "chartReport"; // Возвращаем имя Thymeleaf-шаблона
    }

    //region: Фильтрация
//TODO: Не работает Доделать фильтрацию на сервере

    /**
     * Отбор списка докумументов по фильтрации
     * @return форму listDocuments со списком документов
     */
    @GetMapping ("/filtered")
    public String getListReport(@PathVariable Long organizationId,
                                 @PathVariable Long employeeId,
                                 @RequestParam(value = "documentNumber", required = false) String documentNumber,
                                 @RequestParam(value = "buyer", required = false) String buyer,
                                 @RequestParam(value = "seller", required = false) String seller,
                                 @RequestParam(value = "dateFrom", required = false) LocalDateTime dateFrom,
                                 @RequestParam(value = "dateTo", required = false) LocalDateTime dateTo,
                                 @RequestParam(required = false) String source,
                                 Model model){
        List<Document> documentItems = documentService.findDocumentsFiltered(organizationId, employeeId, documentNumber, buyer, seller, dateFrom, dateTo);
        model.addAttribute("documentItems", documentItems);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));

        return "listDocuments";
    }

    /**
     * Отображение отчета по фильтрации
     * @return форму listReport с данными проданных(купленных) товаров (наименование, количество, общая сумма продажи)
     */
    @GetMapping ("/filteredReport")
    public String getFilteredSearch(@PathVariable Long organizationId,
                                    @PathVariable Long employeeId,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "seller", required = false) String seller,
                                    @RequestParam(value = "buyer", required = false) String buyer,
                                    @RequestParam(value = "dateFrom", required = false) LocalDateTime dateFrom,
                                    @RequestParam(value = "dateTo", required = false) LocalDateTime dateTo,
                                    @RequestParam(value = "templateName", required = false, defaultValue = "listReport") String templateName,
                                    Model model){
        List<DocumentItem> documentItems = documentService.findDocumentItemsFiltered(organizationId, employeeId,name, seller, buyer, dateFrom, dateTo);

        model.addAttribute("documentItems", documentItems);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));

        return templateName;
    }
    //endregion: Фильтрация
}
