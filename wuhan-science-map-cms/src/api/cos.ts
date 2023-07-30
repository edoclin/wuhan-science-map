import { GET } from 'boot/axios'
import COS from 'cos-js-sdk-v5'

export let TENCENT_COS_BUCKET = ''
export let TENCENT_COS_REGION = ''
export const getTemporaryToken = (prefix: string = "*", duration: number = 600) => GET(`/tencentCos/temporaryToken?duration=${duration}&prefix=${encodeURI(prefix)}`)
export const getAccessUrl = (key: string, prefix: string = "*", duration: number = 600) => GET(`/tencentCos/access?duration=${duration}&key=${encodeURI(key)}`)

export const getCOSInstance = () => {
  return new COS({
    getAuthorization: function (options: any, callback: any) {
      getTemporaryToken().then((res: any) => {
        let data = res.data
        callback({
          TmpSecretId: data.temporarySecretId,
          TmpSecretKey: data.temporarySecretKey,
          XCosSecurityToken: data.sessionToken,
          StartTime: data.startTime,
          ExpiredTime: data.expiredTime,
        });
      })
    }
  })
}

export const sliceUploadFile = async(file: any, prefix: string = "wuhan-science") => {
  if (TENCENT_COS_BUCKET === '' || TENCENT_COS_REGION === '') {
    let res:any = await GET(`/tencentCos/config`)
    TENCENT_COS_BUCKET = res.data.bucket
    TENCENT_COS_REGION = res.data.region
  }

  return getCOSInstance().sliceUploadFile({
    Body: file,
    Bucket: TENCENT_COS_BUCKET,
    Key: `${prefix}/${Date.now().toString(32)}_${file.name}`,
    Region: TENCENT_COS_REGION,
  })
}



