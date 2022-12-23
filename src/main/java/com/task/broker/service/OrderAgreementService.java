package com.task.broker.service;

import com.task.broker.repository.OrderAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderAgreementService {

    private final OrderAgreementRepository orderAgreementRepository;

}
