package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.repositories.ResultRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {
    private  final ResultRepository resultRepository;

}
