<?php get_header(); ?>
<?php if (count($res->results->bindings) > 0): ?>
    <table>
        <thead>
            <tr>
                <?php foreach ($res->head->vars as $cell): ?>
                    <th><?php echo htmlspecialchars($cell); ?></th>
                <?php endforeach; ?>
                <?php if (in_array('s', $res->head->vars)): ?>
                    <th>Download</th>
                <?php endif; ?>
            </tr>
        <tbody>
            <?php foreach ($res->results->bindings as $line): ?>
                <tr>
                    <?php foreach ($res->head->vars as $head): ?>
                        <td>
                            <?php if ($line->$head->type == 'uri'): ?>
                                <a href="<?php echo $line->$head->value; ?>"><?php echo $line->$head->value; ?></a>
                            <?php else: ?>
                                <?php echo $line->$head->value; ?>
                            <?php endif; ?>
                        </td>
                    <?php endforeach; ?>
                    <?php if (in_array('s', $res->head->vars)): ?>
                        <td style="text-align: center">'
                            <a href="<?php echo WP_SITEURL; ?>/index.php/blazegraph-search/' . '?download&v=&q=' . urlencode('?s ?o WHERE { {<' . $line->s->value . '> ?s ?o} UNION { ?s <' . $line->s->value . '> ?p} }') . '&format=' . urlencode('application/sparql-results+json') . '" >JSON</a><br/>
                            <a href="<?php echo WP_SITEURL; ?>/index.php/blazegraph-search/' . '?download&v=&q=' . urlencode('?s ?o WHERE { {<' . $line->s->value . '> ?s ?o} UNION { ?s <' . $line->s->value . '> ?p} }') . '&format=' . urlencode('application/sparql-results+xml') . '" >XML</a><br/>
                            <a href="<?php echo WP_SITEURL; ?>/index.php/blazegraph-search/' . '?download&v=&q=' . urlencode('?s ?o WHERE { {<' . $line->s->value . '> ?s ?o} UNION { ?s <' . $line->s->value . '> ?p} }') . '&format=' . urlencode('application/x-binary-rdf-results-table') . '" >RDF BIN TABLE</a><br/>
                            <a href="<?php echo WP_SITEURL; ?>/index.php/blazegraph-search/' . '?download&v=&q=' . urlencode('?s ?o WHERE { {<' . $line->s->value . '> ?s ?o} UNION { ?s <' . $line->s->value . '> ?p} }') . '&format=' . urlencode('text/tab-separated-values') . '" >TSV</a><br/>
                            <a href="<?php echo WP_SITEURL; ?>/index.php/blazegraph-search/' . '?download&v=&q=' . urlencode('?s ?o WHERE { {<' . $line->s->value . '> ?s ?o} UNION { ?s <' . $line->s->value . '> ?p} }') . '&format=' . urlencode('text/csv') . '" >CSV</a>'
                        </td>
                    <?php endif; ?>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
    <?php
 endif; 

