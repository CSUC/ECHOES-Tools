<?php get_header(); ?>
<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'objects_utils') ?>"></script>
<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'place') ?>"></script>
<link rel="stylesheet" href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'semantic'); ?>" />
<?php require_once(dirname(__FILE__) . '/variables.php'); ?>
<div class='main_color container_wrap_first container_wrap fullsize' style='min-height: 565px;'>
    <div class='container'>
        <main role="main" itemprop="mainContentOfPage" class='template-page content av-content-full alpha units'>
            <div class='post-entry post-entry-type-page post-entry-104'>
                <div class='entry-content-wrapper clearfix'>
                    <div class="flex_column av_one_full flex_column_div av-zero-column-padding first avia-builder-el-0 avia-builder-el-no-sibling" style='border-radius:0px;'>
                    <div style="padding-bottom:10px;" class="av-special-heading av-special-heading-h1 avia-builder-el-1 el_before_av_textblock avia-builder-el-first ">
                    <header class="entry-header">
<?php if (isset($title)): ?><h1 class="av-special-heading-tag " itemprop="headline"><?php echo $title; ?></h1><hr/><div class="special-heading-border"><div class="special-heading-inner-border"></div></div><?php endif; ?>
<?php if (isset($subtitle)): ?><h3 class="entry-title person-name"><?php echo $subtitle; ?>  <a target="_blank" href="/exports/download/?subject=<?= $_GET['subject'] ?>&format=xml"><img src="/wp-content/themes/echoes/img/Assets/xml.png"></a>  <a target="_blank" href="/exports/download/?subject=<?= $_GET['subject'] ?>&format=json"><img src="/wp-content/themes/echoes/img/Assets/json.png"></a></h3><?php endif; ?>
                    </header>
                    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
                            <?php $this->render_main_view(); ?>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<?php get_footer(); ?>
