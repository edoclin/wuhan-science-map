import instance from "./ajax";

export const  listBaseSwiper = () => instance.get(`/scienceBase/app/swiper`)

export const  listBaseInfoTopByType = (type: number, regionCode: number) => instance.get(`/scienceBase/app/list/info/top/${type}/${regionCode}`)
export const  listBaseInfoByType = (type: number, current: number, limit: number) => instance.get(`/scienceBase/app/list/info/${type}/${current}/${limit}`)

export const  listBaseTypes = () => instance.get(`/common/baseTypes`)
export const  baseDetailById = (id: string) => instance.get(`/scienceBase/app/detail/${id}`)
export const  searchBaseByKeyword = (keyword: string, regionCode: number) => instance.get(`/scienceBase/app/search/${keyword}/${regionCode}`)
