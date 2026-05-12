package com.nexus.contractflow.mapper;

import com.nexus.contractflow.dto.response.ContratoResponseDTO;
import com.nexus.contractflow.entity.Contrato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContratoMapper {

    @Mapping(target = "fornecedorId", source = "fornecedor.id")
    @Mapping(target = "fornecedorNome", source = "fornecedor.nomeFantasia")
    ContratoResponseDTO toResponse(Contrato contrato);
}
