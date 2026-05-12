package com.nexus.contractflow.mapper;

import com.nexus.contractflow.dto.request.FornecedorRequestDTO;
import com.nexus.contractflow.dto.response.FornecedorResponseDTO;
import com.nexus.contractflow.entity.Fornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FornecedorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "contratos", ignore = true)
    Fornecedor toEntity(FornecedorRequestDTO dto);

    FornecedorResponseDTO toResponse(Fornecedor fornecedor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "contratos", ignore = true)
    void atualizarEntity(FornecedorRequestDTO dto, @MappingTarget Fornecedor entity);
}
