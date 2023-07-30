<template>
  <div>
    <advance-query v-if="advancedQuery.show" :conditions="conditions.map" @onSubmit="queryConditions"
                   @onCancel="advancedQuery.show = false"></advance-query>
    <el-descriptions
        :column="2"
        size="small"
        border
    >
      <el-descriptions-item label-align="center" label="当前展示字段" label-class-name="desc-label">
        <el-select :filterable="true" :clearable="true" :multiple="true" value-key="column" size="small"
                   v-model="displayRows" placeholder="请选择需要展示的字段"
                   style="width: 100%">
          <el-option
              v-for="item in tableData.columns"
              :key="item.column"
              :label="item.label"
              :value="item"
          />
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label-align="center" width="180px" label="数据更新时间">
        <el-link :disabled="disabledFetch" @click="fetchListData">
          <template #icon>
            <el-icon>
              <Refresh/>
            </el-icon>
          </template>
          <div style="color: #919398;font-size: 12px;">
            {{ fetchTime }}
          </div>
        </el-link>
      </el-descriptions-item>
    </el-descriptions>
    <el-table v-loading="tableData.loading" @sort-change='sortTable' border table-layout="auto" stripe
              :data="tableData.data" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30"/>
      <el-table-column v-for="(item, index) in displayRows" :key="index" :sortable="item.sortable" :fixed="item.fixed"
                       :prop="item.column" :label="item.label"></el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #header v-if="selectedData.data.length !== 0">
          <el-popconfirm @confirm="deleteSelected" :title="`确定删除所选中的${selectedData.data.length}条记录?`">
            <template #reference>
              <el-button type="danger" size="small">删除选中</el-button>
            </template>
          </el-popconfirm>
        </template>
        <template #header v-else>
          <el-button type="primary" @click="advancedQuery.show = true" size="small">高级查询</el-button>
        </template>
        <template #default="scope">
          <el-button type="success" link @click="onEdit(scope.row)" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row style="margin-top: 10px">
      <el-col :span="12">
        <el-pagination small background layout="total, sizes, prev, pager, next" :total="page.total"
                       :page-sizes="[10, 20, 50, 100]" v-model:currentPage="page.current"
                       v-model:page-size="page.size"/>
      </el-col>

    </el-row>
  </div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { listActivity, deleteActivityByIds, getActivityConditions, getTableColumns, putActivity } from 'src/api/activity'
import ActivityFormVue from './Form.vue'
import { date } from 'quasar'

const fetchTime = ref('')
const displayRows = ref([])
const disabledFetch = ref(false)
const autoWidth = ref(0)


const page = reactive({
  current: 1,
  total: 0,
  size: 10
})

const conditions = reactive({
  map: []
})

getActivityConditions().then(res => {
  conditions.map = res.data
})

const queryParam = reactive({
  isAsc: false,
  orderColumns: ['createdTime'],
  conditions: []
})

const tableData = reactive({
  data: [],
  columns: [],
  loading: true
})

const fetchListData = (postHandler) => {
  tableData.loading = true
  disabledFetch.value = true
  listActivity(page.current, page.size, { ...queryParam }).then(res => {
    tableData.data = res.data
    page.total = res.count
    fetchTime.value = date.formatDate(Date.now(), 'YYYY年MM月DD日 HH时mm分')

    setTimeout(() => {
      tableData.loading = false
      disabledFetch.value = false
    }, 500)

    if (typeof postHandler === 'function') {
      postHandler()
    }
  }).catch(err => {
    disabledFetch.value = false
  })
}

watch(page, () => fetchListData())

watch(displayRows, () => {
  if (displayRows.value.length <= 3) {
    autoWidth.value = 300
  } else {
    autoWidth.value = 90 * displayRows.value.length
  }
})

fetchListData()

getTableColumns().then(res => {
  tableData.columns = res.data

  displayRows.value = res.data
  if (res.data.length <= 3) {
    autoWidth.value = 300
  } else {
    autoWidth.value = 90 * res.data.length
  }
})

const selectedData = reactive({
  data: []
})
const handleSelectionChange = (value) => {
  selectedData.data = [...value]
}

const deleteSelected = () => {
  let ids = []
  selectedData.data.forEach(item => ids.push(item.id))
  deleteActivityByIds({ ids }).then(res => {
    ElMessage({
      type: 'success',
      message: res.data
    })

    fetchListData()
  })
}

const sortTable = (column) => {
  queryParam.isAsc = column.order === 'ascending'
  queryParam.orderColumns = [column.prop]
  fetchListData()
}

const advancedQuery = reactive({
  show: false,
})

const queryConditions = ({
  conditions,
  createdBetween,
  updatedBetween
}) => {
  queryParam.createdBetween = createdBetween
  queryParam.updatedBetween = updatedBetween
  queryParam.conditions = conditions

  fetchListData(() => {
    advancedQuery.show = false
  })
}

const bus = inject('bus')

const onEdit = (record) => {
  bus.emit('edit-item', {
    record,
    component: ActivityFormVue,
    title: '活动编辑',
    name: record.id
  })
}

bus.on('update-activity-table', () => fetchListData())
</script>
<style>

</style>
