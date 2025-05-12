package com.ethiopianairlines.pilot.data.mapper

import com.ethiopianairlines.pilot.data.model.CreateQuestionRequestDto
import com.ethiopianairlines.pilot.data.model.QuestionResponseDto
import com.ethiopianairlines.pilot.domain.model.Question as DomainQuestion

object QuestionMapper {
    fun toDomain(dto: QuestionResponseDto): DomainQuestion {
        return DomainQuestion(
            id = dto.id ?: "",
            examId = dto.examId ?: "",
            text = dto.text ?: "",
            options = dto.options ?: emptyList(),
            correctAnswerIndex = dto.correctAnswer,
            explanation = dto.explanation ?: "",
//            createdAt = dto.createdAt,
//            updatedAt = dto.updatedAt
        )
    }

    fun fromDomainToCreateRequestDto(domain: DomainQuestion): CreateQuestionRequestDto {
        return CreateQuestionRequestDto(
            text = domain.text,
            options = domain.options,
            correctAnswer = domain.correctAnswerIndex,
            explanation = domain.explanation
        )
    }
}