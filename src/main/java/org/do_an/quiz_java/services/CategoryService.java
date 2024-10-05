package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.model.Category;
import org.do_an.quiz_java.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private  final CategoryRepository categoryRepository;

    public Category find(Integer id){
        if (categoryRepository.findById(id).isPresent()) {
            return categoryRepository.findById(id).get();

        } else {
            System.out.println("No value present in Optional");
            return null;
        }
    }
}
