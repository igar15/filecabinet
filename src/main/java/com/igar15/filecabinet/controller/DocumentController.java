package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.*;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.*;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @Autowired
    private InternalDispatchService internalDispatchService;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/list")
    public String showAll(@RequestParam(name = "decimalNumber", required = false) String decimalNumber,
                          @RequestParam(name = "name", required = false) String name,
                          @RequestParam(name = "department", required = false) String department,
                          @RequestParam(name = "originalHolder", required = false) String originalHolder,
                          @RequestParam(name = "inventoryNumber", required = false) String inventoryNumber,
                          @RequestParam(name = "status", required = false) String status,
                          @RequestParam(name = "stage", required = false) String stage,
                          @RequestParam(name = "form", required = false) String form,
                          @RequestParam(name = "after", required = false) String after,
                          @RequestParam(name = "before", required = false) String before,
                          @SortDefault("decimalNumber") Pageable pageable,
                          Model model) {
        decimalNumber = "".equals(decimalNumber) ? null : decimalNumber;
        name = "".equals(name) ? null : name;
        inventoryNumber = "".equals(inventoryNumber) ? null : inventoryNumber;
        status = "".equals(status) ? null : status;
        stage = "".equals(stage) ? null : stage;
        form = "".equals(form) ? null : form;
        department = "".equals(department) ? null : department;
        originalHolder = "".equals(originalHolder) ? null : originalHolder;
        LocalDate afterDate = (after == null || "".equals(after)) ? LocalDate.of(1900, 1, 1) : LocalDate.parse(after);
        LocalDate beforeDate = (before == null || "".equals(before)) ? LocalDate.of(2050, 1, 1) : LocalDate.parse(before);

        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("department", department);
        model.addAttribute("originalHolders", companyService.findAll());
        model.addAttribute("originalHolder", originalHolder);
        model.addAttribute("status", status);
        model.addAttribute("stage", stage);
        model.addAttribute("form", form);

        Page<Document> documents = null;

        if (checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form, department, originalHolder)) {
            documents = documentRepository.findAllByReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(afterDate, beforeDate, pageable);
        }
        else if(checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form, department)) {
            documents = documentRepository.findAllByOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(originalHolder, afterDate, beforeDate, pageable);
        }
        else if(checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form, originalHolder)) {
            documents = documentRepository.findAllByDepartment_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(department, afterDate, beforeDate, pageable);
        }
        else if(checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form)) {
            documents = documentRepository.findAllByDepartment_NameAndOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(department, originalHolder, afterDate, beforeDate, pageable);
        }
        else {
            Document document = new Document();
            Integer inventoryNumberInt = null;
            try {
                inventoryNumberInt = inventoryNumber == null ? null : Integer.valueOf(inventoryNumber);
            } catch (NumberFormatException e) {
                inventoryNumberInt = -1;
            }
            Status docStatus = status == null ? null : Status.valueOf(status);
            Stage docStage = stage == null ? null : Stage.valueOf(stage);
            Form docForm = form == null ? null : Form.valueOf(form);
            document.setDecimalNumber(decimalNumber);
            document.setName(name);
            document.setDepartment(departmentService.findByName(department));
            document.setOriginalHolder(companyService.findByName(originalHolder));
            document.setInventoryNumber(inventoryNumberInt);
            document.setStatus(docStatus);
            document.setStage(docStage);
            document.setForm(docForm);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("decimalNumber", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
            Example<Document> example = Example.of(document, matcher);
            documents = documentRepository.findAll(example, pageable);
            if (after != null || !"".equals(after) || before != null || !"".equals(before)) {
                List<Document> collect = documents.get()
                        .filter(doc -> (doc.getReceiptDate().compareTo(afterDate) >= 0) && (doc.getReceiptDate().compareTo(beforeDate) <= 0))
                        .collect(Collectors.toList());
                documents = new PageImpl<>(collect, pageable, pageable.getOffset());
            }
        }
        model.addAttribute("documents", documents);
        return "/documents/document-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("document", new Document());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("companies", companyService.findAll());
        return "/documents/document-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("document") Document document, BindingResult bindingResult, Model model) {
        checkDecimalNumberOnDuplicate(document, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("companies", companyService.findAll());
            return "/documents/document-form";
        }
        if (document.isNew()) {
            documentService.create(document);
        } else {
//            document.setExternalDispatches(documentService.findById(document.getId()).getExternalDispatches());
//            document.setInternalDispatches(documentService.findById(document.getId()).getInternalDispatches());
//            documentService.update(document);
//            documentService.updateWithout(document);
            if (document.getSheetsAmount() == null) {
                document.setSheetsAmount(0);
            }
            if (document.getA4Amount() == null) {
                document.setA4Amount(0);
            }
            documentRepository.updateDocument(document.getId(), document.getName(), document.getDecimalNumber(), document.getInventoryNumber(),
                    document.getReceiptDate(), document.getStatus(), document.getForm(), document.getStage(), document.getSheetsAmount(),
                    document.getFormat(), document.getA4Amount(), document.getDepartment(), document.getOriginalHolder());
//            documentRepository.updDoc(document.getId(), document.getName(), document.getDecimalNumber(), document.getInventoryNumber(),
//                    document.getReceiptDate(), document.getStatus(), document.getForm(), document.getStage(), document.getSheetsAmount());
        }
        return "redirect:/documents/showDocumentInfo/" + document.getId();
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("companies", companyService.findAll());
        return "/documents/document-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        documentService.deleteById(id);
        return "redirect:/documents/list";
    }

    @GetMapping("/showDocumentInfo/{id}")
    public String showDocumentInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        return "/documents/document-info";
    }

    @GetMapping("/showChanges/{id}")
    public String showChanges(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        return "documents/document-changenotices";
    }

    @GetMapping("/removeChange/{id}/{changeId}")
    public String removeChange(@PathVariable("id") int id, @PathVariable("changeId") int changeId, Model model) {
        ChangeNotice changeNotice = changeNoticeService.findById(changeId);
        if (changeNotice.getDocuments().size() == 1) {
            model.addAttribute("document", documentService.findById(id));
            String errorMessage = "Change notice " + changeNotice.getName() + " can not exist without any documents!";
            model.addAttribute("errorMessage", errorMessage);
        }
        else {
            Document document = documentService.findById(id);
            Optional<Map.Entry<Integer, ChangeNotice>> found = document.getChangeNotices().entrySet().stream()
                    .filter(entry -> entry.getValue().equals(changeNotice))
                    .findFirst();
            if (found.isPresent()) {
                document.getChangeNotices().remove(found.get().getKey());
                documentService.update(document);
            }
            model.addAttribute("document", document);
        }
        return "documents/document-changenotices";
    }

    @GetMapping("/showExternalDispatches/{id}")
    public String showExternalDispatches(@PathVariable("id") int id, Model model) {
        Document document = documentService.findById(id);
        LinkedHashMap<ExternalDispatch, Boolean> sortedMap = document.getExternalDispatches().entrySet().stream()
                .sorted(Map.Entry.<ExternalDispatch, Boolean>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        document.setExternalDispatches(sortedMap);
        model.addAttribute("document", document);
        return "/documents/document-externaldispatches";
    }

    @GetMapping("/deregisterExternal/{id}/{externalId}")
    public String deregisterExternal(@PathVariable("id") int id, @PathVariable("externalId") int externalId, Model model) {
        Document document = documentService.findById(id);
        document.getExternalDispatches().keySet()
                .forEach(externalDispatch -> {
                    if (externalDispatch.getId().equals(externalId)) {
                        document.getExternalDispatches().put(externalDispatch, false);
                    }
                });
        documentService.update(document);
        model.addAttribute("document", document);
        return "/documents/document-externaldispatches";
    }

    @GetMapping("/showInternalDispatches/{id}")
    public String showInternalDispatches(@PathVariable("id") int id, Model model) {
        Document document = documentService.findById(id);
        LinkedHashMap<InternalDispatch, Boolean> sortedMap = document.getInternalDispatches().entrySet().stream()
                .sorted(Map.Entry.<InternalDispatch, Boolean>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        document.setInternalDispatches(sortedMap);
        model.addAttribute("document", document);
        return "/documents/document-internaldispatches";
    }

    @GetMapping("/deregisterInternal/{id}/{internalId}")
    public String deregisterInternal(@PathVariable("id") int id, @PathVariable("internalId") int internalId, Model model) {
        Document document = documentService.findById(id);
        document.getInternalDispatches().keySet()
                .forEach(internalDispatch -> {
                    if (internalDispatch.getId().equals(internalId)) {
                        document.getInternalDispatches().put(internalDispatch, false);
                    }
                });
        documentService.update(document);
        model.addAttribute("document", document);
        return "/documents/document-internaldispatches";
    }

    @GetMapping("/deregisterAlbum/{id}/{internalId}")
    public String deregisterAlbum(@PathVariable("id") int id,
                                  @PathVariable("internalId") int internalId) {
        InternalDispatch internalDispatch = internalDispatchService.findById(internalId);
        internalDispatch.setIsActive(false);
        internalDispatch.getDocuments().keySet()
                .forEach(key -> internalDispatch.getDocuments().put(key, false));
        internalDispatchService.update(internalDispatch);
        return "redirect:/documents/showInternalDispatches/" + id;
    }

    @GetMapping("/showApplicabilities/{id}")
    public String showApplicabilities(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        return ("/documents/document-applicabilities");
    }

    @GetMapping("/removeApplicability/{id}/{applicabilityId}")
    public String removeApplicability(@PathVariable("id") int id, @PathVariable("applicabilityId") int applicabilityId, Model model) {
        Document document = documentService.findById(id);
        document.setApplicabilities(document.getApplicabilities().stream()
                .filter(doc -> doc.getId() != applicabilityId)
                .collect(Collectors.toSet()));
        documentService.update(document);
        model.addAttribute("document", document);
        return "/documents/document-applicabilities";
    }

    @PostMapping("/addApplicability/{id}")
    public String addApplicability(@PathVariable("id") int id,
                                   @RequestParam(name = "newApplicability") String newApplicability,
                                   Model model) {
        String errorMessage = null;
        Document document = documentService.findById(id);
        if (newApplicability == null || newApplicability.trim().isEmpty()) {
            errorMessage = "Decimal number must not be empty";
        }
        else {
            try {
                Document applicability = documentService.findByDecimalNumber(newApplicability);
                if (document.getApplicabilities().contains(applicability)) {
                    errorMessage = "Applicability already added";
                }
                else {
                    document.getApplicabilities().add(applicability);
                    documentService.update(document);
                    model.addAttribute("document", document);
                    return "document-applicabilities";
                }

            } catch (NotFoundException e) {
                errorMessage = "Document not found";
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("document", document);
        return "/documents/document-applicabilities";
    }

    @GetMapping("/deregisterExternalWithIncomings/{id}/{externalId}")
    public String deregisterExternalWithIncomings(@PathVariable("id") int id,
                                                  @PathVariable("externalId") int externalId,
                                                  Model model) {

        Document mainDocument = documentService.findById(id);
        ExternalDispatch mainExternal = externalDispatchService.findById(externalId);

        mainDocument.getExternalDispatches().keySet()
                .forEach(externalDispatch -> {
                    if (externalDispatch.getId().equals(externalId)) {
                        mainDocument.getExternalDispatches().put(externalDispatch, false);
                    }
                });
        documentService.update(mainDocument);

        List<Document> documentsInMainDocument = documentRepository.findByApplicabilities_DecimalNumber(mainDocument.getDecimalNumber());
        Queue<Document> documents = new LinkedList<>(documentsInMainDocument);
        while (!documents.isEmpty()) {
            Document tempDocument = documents.remove();
            AtomicBoolean needDeregister = new AtomicBoolean(true);

            Set<Document> tempDocumentApplicabilities = tempDocument.getApplicabilities();
            tempDocumentApplicabilities.forEach(document -> {
                Map<ExternalDispatch, Boolean> externalDispatches = document.getExternalDispatches();
                externalDispatches.entrySet().forEach(entry -> {
                    if (entry.getValue()) {
                        if (entry.getKey().getCompany().getName().equals(mainExternal.getCompany().getName())) {
                            needDeregister.set(false);
                        }
                    }
                });
            });

            if (needDeregister.get()) {
                tempDocument.getExternalDispatches().keySet()
                        .forEach(externalDispatch -> {
                            if (externalDispatch.getCompany().getName().equals(mainExternal.getCompany().getName())) {
                                tempDocument.getExternalDispatches().put(externalDispatch, false);
                            }
                        });
                documentService.update(tempDocument);
                documents.addAll(documentRepository.findByApplicabilities_DecimalNumber(tempDocument.getDecimalNumber()));
            }
        }
        model.addAttribute("document", mainDocument);
        return "/documents/document-externaldispatches";
    }





    private boolean checkParamsOnNull(String... params) {
        return Arrays.stream(params)
                .allMatch(Objects::isNull);
    }

    private void checkDecimalNumberOnDuplicate(Document obj, BindingResult bindingResult) {
        boolean isUnique = true;
        Document document = documentRepository.findByDecimalNumber(obj.getDecimalNumber()).orElse(null);

        if (obj.isNew()) {
            isUnique = document == null;
        }
        else if (document != null && !document.getId().equals(obj.getId())) {
            isUnique = false;
        }
        if (!isUnique) {
            bindingResult.rejectValue("decimalNumber", "error.document", "Document already exist");
        }
    }

}
