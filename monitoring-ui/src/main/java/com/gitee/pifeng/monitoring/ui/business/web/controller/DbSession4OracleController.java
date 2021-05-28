package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbSession4OracleService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4OracleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * Oracle数据库会话
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 12:37
 */
@Controller
@Api(tags = "数据库会话.Oracle")
@RequestMapping("/db-session4oracle")
public class DbSession4OracleController {

    /**
     * Oracle数据库会话服务类
     */
    @Autowired
    private IDbSession4OracleService dbSession4OracleService;

    /**
     * <p>
     * 获取会话列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return layUiAdmin响应对象
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @ApiOperation(value = "获取会话列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "id", value = "数据库ID", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/get-session-list")
    @ResponseBody
    public LayUiAdminResultVo getSessionList(Long current, Long size, Long id) throws SQLException {
        Page<DbSession4OracleVo> page = this.dbSession4OracleService.getSessionList(current, size, id);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param dbSession4OracleVos Oracle数据库会话
     * @param id                  数据库ID
     * @return layUiAdmin响应对象
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/25 17:03
     */
    @ApiOperation(value = "结束会话")
    @DeleteMapping("/destroy-session")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "数据库ID", required = true, paramType = "query", dataType = "long")})
    @ResponseBody
    public LayUiAdminResultVo destroySession(@RequestBody List<DbSession4OracleVo> dbSession4OracleVos, Long id) throws SQLException {
        return this.dbSession4OracleService.destroySession(dbSession4OracleVos, id);
    }

}