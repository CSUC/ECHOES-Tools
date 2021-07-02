<?php get_header(); ?>
<?php require_once(dirname(__FILE__) . '/variables.php'); ?>
<style>
#searchform #s {
    border-color: #e0b13a;
}
</style>
<div class='main_color container_wrap_first container_wrap fullsize'>
    <div class='container'>
        <main role="main" itemprop="mainContentOfPage" class='template-page content av-content-full alpha units'>
            <div class='post-entry post-entry-type-page post-entry-104'>
                <div class='entry-content-wrapper clearfix'>
                    <div class="flex_column av_one_full flex_column_div av-zero-column-padding first avia-builder-el-0 avia-builder-el-no-sibling" style='border-radius:0px;'>
                    <div style="padding-bottom:10px;" class="av-special-heading av-special-heading-h1 avia-builder-el-1 el_before_av_textblock avia-builder-el-first ">
                            <?php $this->render_main_view(); ?>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<?php get_footer(); ?>
