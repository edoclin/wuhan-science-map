import instance from "./ajax";


export const listRegionsByPCode = (pCode: number) => instance.get(`/region/listRegionAndChildren/${pCode}`)


export const listOffspringCodeByCode = (pCode: number, isMap: boolean) => instance.get(`/region/app/offspringCodeByParentCode/${pCode}/${isMap}`)