package com.nexus.contractflow.service;

import com.nexus.contractflow.entity.Contrato;
import com.nexus.contractflow.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContratoScheduler {

    private final ContratoRepository contratoRepository;

    /**
     * Roda todo dia à meia-noite (00:00:00) - timezone do servidor.
     * Atualiza contratos ATIVOS cuja data_fim já passou para status VENCIDO.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void atualizarContratosVencidos() {
        long inicio = System.currentTimeMillis();
        LocalDate hoje = LocalDate.now();
        log.info("Iniciando job de atualização de contratos vencidos. Data de referência: {}", hoje);

        List<Contrato> aVencer = contratoRepository.buscarAtivosComDataFimMenorQue(hoje);

        if (aVencer.isEmpty()) {
            log.info("Nenhum contrato precisa ser atualizado. Job concluído em {}ms.",
                    System.currentTimeMillis() - inicio);
            return;
        }

        aVencer.forEach(c -> log.info("Contrato {} mudou para VENCIDO pelo sistema (dataFim={}).",
                c.getNumeroContrato(), c.getDataFim()));

        int atualizados = contratoRepository.atualizarContratosVencidos(hoje);
        log.info("Job concluído: {} contratos atualizados para VENCIDO em {}ms.",
                atualizados, System.currentTimeMillis() - inicio);
    }
}
