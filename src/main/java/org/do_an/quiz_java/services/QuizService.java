package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.QuestionResultDTO;
import org.do_an.quiz_java.dto.QuizDTO;
import org.do_an.quiz_java.dto.ResultDTO;
import org.do_an.quiz_java.dto.UpdateQuizDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.*;
import org.do_an.quiz_java.repositories.CategoryRepository;
import org.do_an.quiz_java.repositories.FavoriteQuizRepository;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.do_an.quiz_java.respones.category.CategoryQuizResponse;
import org.do_an.quiz_java.respones.quiz.QuizResponse;
import org.do_an.quiz_java.respones.result.ResultResponse;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuizService {
    private final ModelMapper modelMapper;

    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final QuestionResultService questionResultService;
    private final ResultService resultService;
    private final CloudinaryService cloudinaryService;
    private final CategoryRepository categoryRepository;
    private final CompetitionQuizService competititonQuizService;
    private final FavoriteQuizRepository favoriteQuizRepository;
    @Caching(
            put = @CachePut(value = "quiz", key = "'findAllQuiz'"),
            evict = @CacheEvict(value = "quiz", allEntries = true)
    )
    public Quiz save(QuizDTO quizDTO,  User user) {
            Quiz quiz = Quiz.builder()
                    .title(quizDTO.getTitle())
                    .category(categoryService.find(quizDTO.getCategory_id()))
                    .createdBy(user)
                    .description(quizDTO.getDescription())
                    .isPublished(quizDTO.getIsPublished())
                    .totalQuestions(quizDTO.getQuestions().size())
                    .createdBy(user).build();
    //        quizRepository.save(quiz);

            quiz.setQuestions(questionService.save(quizDTO.getQuestions(),quiz));
            return quizRepository.save(quiz);

    }
    @Caching(
            put = @CachePut(value = "quiz", key = "'findAllQuiz'"),
            evict = @CacheEvict(value = "quiz", allEntries = true)
    )
        public QuizResponse updateQuizWithImage(MultipartFile file, Integer quizId) throws DataNotFoundException {
            Quiz quiz = findByQuizId(quizId);
            try {
                String imageUrl = storeFile(file);
                log.error("Error while uploading image" + imageUrl);
                quiz.setImage(imageUrl);
            } catch (Exception e) {
                log.error("Error while uploading image");
            }
            quizRepository.save(quiz);
            return QuizResponse.fromEntity(quizRepository.findByQuizId(quizId));
        }

    public Page<Quiz> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public Quiz findByQuizId(Integer id) throws DataNotFoundException {

        return quizRepository.findByQuizId(id);
    }

    public Page<Quiz> findAllPublished(Pageable pageable) {
        return quizRepository.findByIsPublishedTrue(pageable);
    }


    //    public Quiz update(Quiz newQuiz) throws TokenExpiredException, DataNotFoundException {
//        Optional<Quiz> currentQuiz = find(newQuiz.getId());
//
//        mergeQuizzes(currentQuiz, newQuiz);
//        return quizRepository.save(currentQuiz);
//    }
    private void mergeQuizzes(Quiz currentQuiz, Quiz newQuiz) {
        currentQuiz.setTitle(newQuiz.getTitle());
        currentQuiz.setDescription(newQuiz.getDescription());
    }

    public Page<Quiz> search(String query, Pageable pageable) {
        return quizRepository.searchByName(query, pageable);
    }

    public List<QuizResponse> findQuizByUser(User user) {
        return quizRepository.findByCreatedBy(user).stream()
                .map(QuizResponse::fromEntity)
                .collect(Collectors.toList());

    }
    public CategoryQuizResponse findQuizByCategory(Integer categoryId) {
        Category category = categoryService.find(categoryId);
        List<Quiz> quizzes = quizRepository.findByCategory(category);
        return CategoryQuizResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .count(quizzes.size())
                .quizResponses(QuizResponse.fromEntities(quizzes))
                .build();

    }
    public List<CategoryQuizResponse> getAllQuizByCategory() {
        List<Category> categories = categoryService.findAll();
        List<CategoryQuizResponse> categoryQuizResponses = new ArrayList<>();
        for(Category category : categories) {
            List<Quiz> quizzes = quizRepository.findByCategory(category);
            if(quizzes.isEmpty()) {
                continue;
            }
            CategoryQuizResponse categoryQuizResponse = CategoryQuizResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .count(quizzes.size())
                    .quizResponses(QuizResponse.fromEntities(quizzes))
                    .build();
            categoryQuizResponses.add(categoryQuizResponse);
        }
        return categoryQuizResponses;
    }

    @Cacheable(value = "quiz" , key = "'findAllQuiz'")
    @Transactional
    public List<QuizResponse> findAllQuiz() {
        return quizRepository.findAll().stream()
                .map(QuizResponse::fromEntity)
                .collect(Collectors.toList());

    }
    private String storeFile(MultipartFile file) throws Exception {
        if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
            return "False";
        }
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
            return "False";
        }
        String folderName = "image";
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, folderName);
        return uploadResult.get("url").toString();

    }
    @CacheEvict(value = "quiz", allEntries = true)
    public void delete(Integer id) {
        quizRepository.deleteById(id);
    }
    public ResultResponse submit(ResultDTO resultDTO, User user) {
        return resultService.submit(resultDTO,user);
    }

    @Caching(
            put = @CachePut(value = "quiz", key = "'findAllQuiz'"),
            evict = @CacheEvict(value = "quiz", allEntries = true)
    )
    public QuizResponse update(UpdateQuizDTO updateQuizDTO) throws DataNotFoundException {
        Quiz existingQuiz = findByQuizId(updateQuizDTO.getId());
        Category category = categoryRepository.findById(updateQuizDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        if(existingQuiz == null) {
            throw new DataNotFoundException("Quiz not found");
        }
        if (updateQuizDTO.getTitle() != null){
            existingQuiz.setTitle(updateQuizDTO.getTitle());
        }

        if (updateQuizDTO.getDescription() != null){
            existingQuiz.setDescription(updateQuizDTO.getDescription());
        }
        if(updateQuizDTO.getIsPublished() != null){
            existingQuiz.setIsPublished(updateQuizDTO.getIsPublished());
        }
        if(updateQuizDTO.getQuestions() != null){
            existingQuiz.setTotalQuestions(updateQuizDTO.getQuestions().size());
            existingQuiz.setQuestions(questionService.update(updateQuizDTO.getQuestions(),existingQuiz));
//            questionService.update(updateQuizDTO.getQuestions(),existingQuiz);
//
//            existingQuiz.setQuestions(updateQuizDTO.getQuestions());
        }
        existingQuiz.setCategory(category);

        quizRepository.save(existingQuiz);
        return QuizResponse.fromEntity(existingQuiz);
    }
    @Caching(
            put = @CachePut(value = "quiz", key = "'findAllQuiz'"),
            evict = @CacheEvict(value = "quiz", allEntries = true)
    )
    public QuizResponse publishQuiz(Integer quizId) throws DataNotFoundException {
        Quiz quiz = findByQuizId(quizId);
        quiz.setIsPublished(true);
        quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }
    @Caching(
            put = @CachePut(value = "quiz", key = "'findAllQuiz'"),
            evict = @CacheEvict(value = "quiz", allEntries = true)
    )
    public QuizResponse unPublishQuiz(Integer quizId) throws DataNotFoundException {
        Quiz quiz = findByQuizId(quizId);
        quiz.setIsPublished(false);
        quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }

    @CacheEvict(value = "quiz", allEntries = true)
    public void clearAllQuizCache() {
        System.out.println("Clearing all quiz cache...");
    }

    public List<QuizResponse> searchByCategory(String filter, Integer categoryId) {
        return quizRepository.findByCategoryNameContaining(filter,categoryId).stream()
                .map(QuizResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void addFavoriteQuiz(User user, Quiz quiz) throws DataNotFoundException {
        FavoriteQuiz favoriteQuiz = favoriteQuizRepository.findByUserIdAndQuizId(user.getId(), quiz.getId());
        if (favoriteQuiz != null) {
            throw new DataNotFoundException("Quiz already in favorite list");
        }
        FavoriteQuiz newfavoriteQuiz = FavoriteQuiz.builder()
                .user(user)
                .quiz(quiz)
                .build();
        favoriteQuizRepository.save(newfavoriteQuiz);
    }

    public void removeFavoriteQuiz(User user, Integer quizId) throws DataNotFoundException {
        FavoriteQuiz favoriteQuiz = favoriteQuizRepository.findByUserIdAndQuizId(user.getId(), quizId);
        if (favoriteQuiz == null) {
            throw new DataNotFoundException("Quiz not found in favorite list");
        }
        favoriteQuizRepository.delete(favoriteQuiz);
    }

    public List<QuizResponse> findFavoriteQuiz(User user) {
        return favoriteQuizRepository.findByUserId(user.getId()).stream()
                .map(favoriteQuiz -> QuizResponse.fromEntity(favoriteQuiz.getQuiz()))
                .collect(Collectors.toList());
    }

    public boolean isFavorite(User user, Integer quizId) {
        FavoriteQuiz favoriteQuiz = favoriteQuizRepository.findByUserIdAndQuizId(user.getId(), quizId);
        return favoriteQuiz != null;
    }
}
