package com.example.dailywriter.controller;

import com.example.dailywriter.form.MessageForm;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;
import com.example.dailywriter.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final MessageService messageService;

    public HomeController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(Model model) {

        if (!model.containsAttribute("selectedType")) {
            model.addAttribute("selectedType", MessageType.START);
        }

        if (!model.containsAttribute("selectedTone")) {
            model.addAttribute("selectedTone", Tone.NORMAL.name());
        }

        return "index";
    }

    @PostMapping("/generate")
    public String generate(
            @ModelAttribute MessageForm form,
            RedirectAttributes redirectAttributes) {

        if (form.isReportContentEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "今日やったことを入力してください。"
            );

            addFormAttributes(redirectAttributes, form);

            return "redirect:/";
        }

        String message = messageService.generate(
                form.getType(),
                form.getWorkContent()
        );

        redirectAttributes.addFlashAttribute(
                "message",
                message
        );

        addFormAttributes(redirectAttributes, form);

        return "redirect:/";
    }

    private void addFormAttributes(
        RedirectAttributes redirectAttributes,
        MessageForm form) {

        redirectAttributes.addFlashAttribute(
                "selectedType",
                form.getType()
        );

        redirectAttributes.addFlashAttribute(
                "workContent",
                form.getWorkContent()
        );

        redirectAttributes.addFlashAttribute(
                "selectedTone",
                form.getTone() == null
                        ? Tone.NORMAL.name()
                        : form.getTone().name()
        );
    }
}