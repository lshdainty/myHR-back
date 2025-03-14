package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.Dues;
import com.lshdainty.myhr.domain.DuesType;
import com.lshdainty.myhr.repository.DuesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DuesService {
    private final DuesRepository duesRepository;

    @Transactional
    public Long save(String name, int amount, DuesType type, String date, String detail) {
        Dues dues = Dues.createDues(name, amount, type, date, detail);
        duesRepository.save(dues);
        return dues.getSeq();
    }

    public List<Dues> findDues() {
        return duesRepository.findDues();
    }

    public List<Dues> findDuesByYear(String year) {
        return duesRepository.findDuesByYear(year);
    }

    @Transactional
    public void deleteDues(Long duesSeq) {
        Dues findDues = duesRepository.findById(duesSeq);
        duesRepository.delete(findDues);
    }
}
