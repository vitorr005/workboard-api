package com.example.WorkBoard.dto;

import com.example.WorkBoard.model.StatusTarefa;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarStatusRequest {
    private StatusTarefa status;

}
