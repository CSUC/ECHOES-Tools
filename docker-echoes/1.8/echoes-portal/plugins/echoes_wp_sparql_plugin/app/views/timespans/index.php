<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<div style='width: 70%; float: left'>
<header class="entry-header">
    <h1 class="av-special-heading-tag " itemprop="headline">List of TimeSpans</h1><div class="special-heading-border"><div class="special-heading-inner-border"></div></div>
    </header>	
    <form action="<?php echo WP_SITEURL; ?>/timespans/search" id="searchform" method="get" class="" style="width: 100%">
        <div style="max-width: 100%">
            <input type="submit" value="" id="searchsubmit" class="button avia-font-entypo-fontello" />
            <input type="text" id="s" name="s" value="" placeholder='Search'/>
        </div>
    </form>
    <?php if (count($bindings) == 0) : ?>
        <h2>There are no timespans</h2>
    <?php else: ?>
        <div id="results" style="width: 100%; margin-top: 10px; padding: 5px;">
            <ul style="list-style-type: none;margin-left: 0;">
                <?php
                foreach ($bindings as $binding):
                    if ($binding->begin->value == $binding->end->value) {
                        $bindingText = $binding->begin->value;
                    } else {
                        $bindingText = $binding->begin->value . ' ~ ' . $binding->end->value;
                    }
                    ?>
                    <li style="margin-left: 0;"><i class="fa fa-clock-o" aria-hidden="true"></i> <a href="<?php echo WP_SITEURL; ?>/timespans/relations/?timespan_name=<?php echo $bindingText; ?>&subject=<?php echo $binding->timeSpan->value; ?>&page=1"><?php echo $bindingText; ?></a></li> 
    <?php endforeach; ?>   
            </ul>
        </div>
    <?php endif; ?> 
    <h2 class="entry-title">Digital Objects over TimeSpans</h2>
<?php require_once dirname(__FILE__) . '/timespan_chart.php'; ?>
</div>
<div style='width: 25%; float: left; margin-left: 5%'>
    <section class="av_textblock_section " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="avia_textblock  " itemprop="text"><h3><span><strong>Refine the results</strong></span></h3>
        </div></section>
    <div class="avia-icon-list-container   avia-builder-el-7  el_after_av_textblock  avia-builder-el-last "><ul class="avia-icon-list avia-icon-list-left av-iconlist-small avia_animate_when_almost_visible avia_start_animation">
            <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char " aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header class="entry-content-header"><div class="av_iconlist_title iconlist_title_small  " itemprop="headline">People</div></header><div class="iconlist_content  " itemprop="text"><ul>
                                <li>Jansen (3)</li>
                                <li>Smith (2)</li>
                                <li>Peter (5)</li>
                            </ul>
                            <p><a href="#">Show more</a></p>
                        </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
            <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char " aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header class="entry-content-header"><div class="av_iconlist_title iconlist_title_small  " itemprop="headline">Places</div></header><div class="iconlist_content  " itemprop="text"><ul>
                                <li>Leiden (89)</li>
                                <li>Barcelona (73)</li>
                                <li>London (62)</li>
                            </ul>
                            <p><a href="#">Show more</a></p>
                        </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
            <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char " aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header class="entry-content-header"><div class="av_iconlist_title iconlist_title_small  " itemprop="headline">Concept</div></header><div class="iconlist_content  " itemprop="text"><ul>
                                <li>INFORMATIEBOEK (4)</li>
                                <li>Monestir de Santes Creus (7)</li>                                    
                                <li>Mas de la Creu (8)</li>
                            </ul>
                            <p><a href="#">Show more</a></p>
                        </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
             <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char " aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header class="entry-content-header"><div class="av_iconlist_title iconlist_title_small  " itemprop="headline">Digital Object</div></header><div class="iconlist_content  " itemprop="text"><ul>
                                <li>Digital Object 1 (8)</li>
                                <li>Digital Object 2 (19)</li>
                                <li>Digital Object 3 (20)</li>
                            </ul>
                            <p><a href="#">Show more</a></p>
                        </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
        </ul></div>
</div>
