/**
 * Api接口常量
 */
const api = {
    homeView: '/index',
    login: '/login',
    login_in: '/login/in',
    login_out: '/login/out',

    job: {
        homeCount: '/job/getHomeCount',
        listPage: '/job/listPage',
        detailHtml: '/page/task-detail',
        detail: '/job/getJobDetail',
        addHtml: '/page/task-add',
        add: '/job/save',
        update: '/job/update',
        start: '/job/start',
        stop: '/job/stop',
        delete: '/job/delete',
        taskCornHtml: '/page/task-corn',
    },
    job_log: {
        listPage: '/job-log/listPage',
        detailHtml: '/page/task-log-detail',
        detail: '/job-log/getLogDetail',
    },
    user: {
        listPage: '/user/listPage',
        save: '/user/save',
        detail: '/user/getUserDetail',
        user_person_html: '/page/user-person',
        getPerson: '/user/getUserPersonDetail',
        update: '/user/update',
        delete: '/user/delete',
        update_pwd: '/user/update-pwd',
        update_power: '/user/update-power',
        add_user_html: '/page/user-add',
        update_pwd_html: '/page/user-edit-pwd',
        update_power_html: '/page/user-edit-power',
        update_detail_html: '/page/user-detail'
    },
    captchaUrl: 'captcha.jpg',
    comm: {
        selUserView: 'common/selUser',
        userMenus: 'index/menus',  // 用户目录菜单（左侧）
        userNavMenus: 'index/navMenus',  // 用户导航菜单（横向）
        fileUpload: 'common/fileUpload'
    },

    gen: {
        tableListData: 'generator/list/tableData',
        customGenerateSetting: 'generator/setting/',
        customGenerateCode: 'generator/custom/generate/code',
        quicklyGenerateCode: 'generator/quickly/generate/code',
        quicklyGenerateCodeBatch: 'generator/quickly/generate/code/batch',
        checkCodeZipIsExists: 'generator/check/codeZip/isExists',
        downloadCodeZip: 'generator/download/codeZip'
    }

};

$.ajaxSetup({
    //完成请求后触发。即在success或error触发后触发
    complete: function (xhr, status) {
        const cookieValue = decodeURIComponent(getCookie("COOKIE_USER_INFO"));
        if (xhr.status === 401) {
            layer.msg('未登录,请先登录', {
                offset: '15px', icon: 1, time: 500
            }, function () {
                location.href = api.login;
            });
        }
        if (xhr.status === 405) {
            layer.msg('权限不足', {
                offset: '15px', icon: 1, time: 500
            }, function () {
            });
        } else if (null === cookieValue || "" === cookieValue) {
            location.href = api.login;
        }
    }
});
åå
