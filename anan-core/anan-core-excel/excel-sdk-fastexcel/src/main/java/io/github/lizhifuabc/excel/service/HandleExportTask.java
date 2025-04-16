package io.github.lizhifuabc.excel.service;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *
 * @author lizhifu
 * @since 2025/2/21
 */
@Slf4j
public class HandleExportTask {
    void handleExportTask(int totalRows) {
        if (totalRows == 0) {
            return;
        }
        // 总数除以每批数量，并向上取整得到批次数
        int batchRows = 2000;
        int batchNum = totalRows / batchRows + (totalRows % batchRows == 0 ? 0 : 1);
        // 总批次数除以并发比例，并向上取整得到并发轮数
        int concurrentRound = batchNum / TaskThreadPool.concurrentRate
                + (batchNum % TaskThreadPool.concurrentRate == 0 ? 0 : 1);;

        log.info("本次报表导出任务-目标数据量：{}条，每批数量：{}，总批次数：{}，并发总轮数：{}", totalRows, batchRows, batchNum, concurrentRound);

    }
}
