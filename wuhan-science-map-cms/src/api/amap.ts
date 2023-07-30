import { amapWebKey } from "src/boot/amap"
import { amapRequest } from "src/boot/axios"

export const regeo  = (lng:number, lat:number) => {
    return new Promise((resolve, reject) => {
        amapRequest.get(`/geocode/regeo?key=${amapWebKey}&location=${lng},${lat}&radius=0&extensions=base&batch=false&roadlevel=1`).then(res => {
                if (res.data.infocode === '10000') {
                    resolve(res.data.regeocode.formatted_address)
                }
        }).catch(err => reject(err))
    })
}
