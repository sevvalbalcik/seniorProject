package com.example.senior_project.service;

import com.example.senior_project.model.EducationalContent;
import com.example.senior_project.model.User;
import com.example.senior_project.repository.EducationalContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationalContentService {
    private final EducationalContentRepository educationalContentRepository;

    @Transactional
    public EducationalContent createContent(EducationalContent content, User author) {
        content.setAuthor(author);
        content.setPublished(false);
        return educationalContentRepository.save(content);
    }

    @Transactional
    public EducationalContent updateContent(Long contentId, EducationalContent updatedContent, User author) {
        EducationalContent existingContent = educationalContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("İçerik bulunamadı"));

        if (!existingContent.getAuthor().equals(author)) {
            throw new RuntimeException("Bu içeriği düzenleme yetkiniz yok");
        }

        existingContent.setTitle(updatedContent.getTitle());
        existingContent.setDescription(updatedContent.getDescription());
        existingContent.setCategory(updatedContent.getCategory());
        existingContent.setContent(updatedContent.getContent());
        existingContent.setResources(updatedContent.getResources());
        existingContent.setVideoUrls(updatedContent.getVideoUrls());
        existingContent.setEstimatedDuration(updatedContent.getEstimatedDuration());
        existingContent.setDifficulty(updatedContent.getDifficulty());

        return educationalContentRepository.save(existingContent);
    }

    @Transactional(readOnly = true)
    public Page<EducationalContent> getAllPublishedContent(Pageable pageable) {
        return educationalContentRepository.findByIsPublishedTrue(pageable);
    }

    @Transactional(readOnly = true)
    public List<EducationalContent> getContentByCategory(String category) {
        return educationalContentRepository.findByCategoryAndIsPublishedTrue(category);
    }

    @Transactional(readOnly = true)
    public EducationalContent getContentById(Long contentId) {
        return educationalContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("İçerik bulunamadı"));
    }

    @Transactional
    public void publishContent(Long contentId, User admin) {
        EducationalContent content = educationalContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("İçerik bulunamadı"));
        
        content.setPublished(true);
        educationalContentRepository.save(content);
    }

    @Transactional
    public void deleteContent(Long contentId, User author) {
        EducationalContent content = educationalContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("İçerik bulunamadı"));

        if (!content.getAuthor().equals(author)) {
            throw new RuntimeException("Bu içeriği silme yetkiniz yok");
        }

        educationalContentRepository.delete(content);
    }

    @Transactional(readOnly = true)
    public List<EducationalContent> searchContent(String keyword) {
        return educationalContentRepository.findByTitleContainingOrDescriptionContainingAndIsPublishedTrue(keyword, keyword);
    }
} 