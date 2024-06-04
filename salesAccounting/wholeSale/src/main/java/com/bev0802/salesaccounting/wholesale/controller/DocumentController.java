package com.bev0802.salesaccounting.wholesale.controller;

import com.bev0802.salesaccounting.wholesale.model.Document;
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
}
