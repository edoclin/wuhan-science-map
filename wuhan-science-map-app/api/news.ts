import instance from "./ajax";

export const  listNewsSwiper = () => instance.get(`/news/app/swiper`)

export const  listNewsInfoTopByType = (type: string) => instance.get(`/news/app/list/info/top/${type}`)
export const  listNewsInfoByType = (type: string, current: number, limit: number) => instance.get(`/news/app/list/info/${type}/${current}/${limit}`)

export const  listNewsTypes = () => instance.get(`/common/newsTypes`)
export const  newsDetailById = (id: string) => instance.get(`/news/app/detail/${id}`)
export const  searchNewsByKeyword = (keyword: string) => instance.get(`/news/app/search/${keyword}`)

