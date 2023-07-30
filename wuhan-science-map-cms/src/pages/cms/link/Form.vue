<template>
  <el-form :rules="rules" :model="form" label-width="120px" ref="formRef">
    <el-form-item label="链接名称" prop="linkName">
      <el-input v-model="form['linkName']"/>
    </el-form-item>
    <el-form-item label="链接地址" prop="url">
      <el-input v-model="form['url']"/>
    </el-form-item>
    <el-form-item label="链接Logo" prop="linkLogo">
      <el-upload :on-preview="handlePreviewUpload" list-type="picture-card" v-model:file-list="fileList"
                 :auto-upload="false" accept="image/*"
                 :on-change="handleChangeFileUpload">
        <el-button type="primary" :loading="uploading">
          {{ uploading ? '上传中...' : (fileList.length === 1 ? '重新上传' : '点击上传') }}
        </el-button>
        <template #tip>
          <div class="el-upload__tip">
            <el-progress
                style="width: 202px"
                v-if="uploading"
                :percentage="100"
                :indeterminate="true"
                :duration="1"
                :format="() => ''"
            />
            <span v-else>请选择图片文件上传</span>
          </div>
        </template>
      </el-upload>
    </el-form-item>
    <el-form-item label="当前状态">
      <el-select v-model="form['status']" placeholder="请选择状态" style="width: 100%">
        <el-option v-for="status in status" :key="status.key" :label="status.value"
                   :value="status.key"/>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit(formRef)">{{ data ? '更新' : '创建' }}</el-button>
      <el-button v-if="!data" @click="onResetForm(formRef)">重置</el-button>
    </el-form-item>
  </el-form>
</template>
<script setup>
import { postLink, putLink } from 'src/api/link'
import { ElMessage } from 'element-plus'

import { useMapState } from 'src/stores'
import { useCommonStore } from 'src/stores/common_store'
import { openURL } from 'quasar'
import { getAccessUrl, sliceUploadFile } from 'src/api/cos'

const { status } = useMapState(useCommonStore, ['status'])

const bus = inject('bus')

const editorRef4Desc = shallowRef()
const valueHtml4Desc = ref('')

onBeforeUnmount(() => {
  let editor = editorRef4Desc.value
  if (editor != null) editor.destroy()
})

const handleCreated4Desc = (editor) => {
  editorRef4Desc.value = editor
}

const toolbarConfig4Desc = reactive({
  excludeKeys: [
    'fullScreen'
  ]
})

const editorConfig4Desc = reactive({
  placeholder: '请输入讲师介绍',
  MENU_CONF: {
    'uploadImage': {
      maxFileSize: 1024 * 1024 * 10,
      maxNumberOfFiles: 100,
      allowedFileTypes: ['image/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 10 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    },
    'uploadVideo': {
      maxFileSize: 1024 * 1024 * 100 * 10,
      maxNumberOfFiles: 10,
      allowedFileTypes: ['video/*', 'audio/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 20 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    }
  }
})

const uploading = ref(false)
const fileList = ref([])

const handlePreviewUpload = (e) => {
  openURL(e.url)
}

const handleChangeFileUpload = (uploadFile) => {
  uploading.value = true
  fileList.value = []
  sliceUploadFile(uploadFile.raw, 'link-logo').then(data => {
    form['linkLogo'] = data['Key']
    ElMessage({
      type: 'success',
      message: '上传成功'
    })
    uploading.value = false
    getAccessUrl(data['Key']).then(res => {
      fileList.value.push({
        name: '',
        url: res.data
      })
    })
  }).catch(err => {
    uploading.value = false
    ElMessage({
      type: 'error',
      message: '服务器繁忙,请重试!'
    })
  })
}

const formRef = ref()

const form = reactive({})

const rules = reactive({
  'linkName': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入链接名称'
    },
  ],
  'url': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入链接地址'
    },
  ],
  'linkLogo': [
    {
      required: true,
      trigger: 'blur',
      message: '请上传链接Logo'
    },
  ],
})
const onResetForm = (formEl) => {
  if (formEl) {
    formEl.resetFields()
  }
}

const onSubmit = (formEl) => {
  formEl.validate(async (valid) => {
    if (valid) {
      if (props.data && JSON.stringify(props.data) !== '{}') {
        await putLink(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
        })
      } else {
        await postLink(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      }
      bus.emit('update-link-table')
      bus.emit('update-base-link-selector')
    }
  })
}

const props = defineProps({
  data: {}
})
if (props.data && JSON.stringify(props.data) !== '{}') {
  form['status'] = status.value.find(item => item.value === props.data['status']).key
  form['id'] = props.data['id']
  form['url'] = props.data['url']
  form['linkName'] = props.data['linkName']
  form['linkLogo'] = props.data['linkLogo']
  fileList.value = [{
    name: '',
    url: props.data['linkLogoUrl']
  }]
}

</script>
